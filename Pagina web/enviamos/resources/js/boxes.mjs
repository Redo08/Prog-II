export default class Boxes {
  static #table
  static #modal
  static #currentOption
  static #form
  static #customers

  constructor() {
    throw new Error('No requiere instancias, todos los métodos son estáticos. Use Boxes.init()')
  }

  static async init() {
    try {
      Boxes.#form = await Helpers.fetchText('./resources/html/boxes.html')

      // Acceder a la información de usuarios
      let response = await Helpers.fetchJSON(`${urlAPI}/client`)
      if (response.message != 'ok') {
        throw new Exception(response) // JS no cuenta con una clase exception
      }

      // Crear las opciones para un select de clientes
      Boxes.#customers = Helpers.toOptionList({
        items: response.data,
        value: 'id',
        text: 'name',
        firstOption: 'Choose a client',
      })

      //Intentar cargar la info
      response = await Helpers.fetchJSON(`${urlAPI}/box`)
      if (response.message != 'ok') {
        throw new Error(response.message)
      }

      // Cargar en un atributo de clase el Array de tipos Estados
      // Aqui recibimos todo el mensaje
      const statusResponse = await Helpers.fetchJSON(`${urlAPI}/delivery/status`)
      if (statusResponse.message != 'ok') {
        throw new Error(statusResponse.message)
      }
      // Tenemos que pasar ese mensaje pues solo necesitamos la data que es el array
      let statusType = statusResponse.data

      // Agregar al <main> la capa que correspondera a la tabla
      document.querySelector('main').innerHTML = `<div id="table-container" class="m-2"></div>`

      Boxes.#table = new Tabulator('#table-container', {
        height: 'auto', // establecer la altura para habilitar el DOM virtual y mejorar la velocidad de procesamiento
        data: response.data,
        layout: 'fitData', // ajustar columnas al ancho disponible. También fitData|fitDataFill|fitDataStretch|fitDataTable|fitColumns
        columns: [
          // definir las columnas de la tabla, para tipos datetime se utiliza formatDateTime definido en index.mjs
          { formatter: editRowButton, width: 40, hozAlign: 'center', cellClick: Boxes.#editRowClick },
          { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: Boxes.#deleteRowClick },
          { title: 'NumGuide', field: 'numGuide', hozAlign: 'center', width: 90 },
          { title: 'Sender', field: 'sender.name', width: 200 },
          { title: 'Addressee', field: 'addressee.name', width: 300 },
          { title: 'Content', field: 'content', width: 300 },
          { title: 'Value', field: 'value', hozAlign: 'right', width: 100, formatter: 'money' },
          { title: 'Weight', field: 'weight', hozAlign: 'center', width: 80 },
          { title: 'Price', field: 'payment', hozAlign: 'right', width: 100, formatter: 'money' },
          { title: 'Fragile', field: 'isFragile', width: 80, formatter: 'tickCross' },
          {
            title: 'Final Status',
            field: 'status',
            width: 300,
            formatter: function (cell) {
              // Conseguimos el valor de la celda
              let status = cell.getValue()
              // Obtención del ultimo estado
              if (Array.isArray(status) && status.length > 0) {
                let lastStatus = status[status.length - 1]
                // Buscamos en el array, la llave actual del ultimo estado
                let finalStatus = statusType.find(current => current.key === lastStatus.deliveryStatus)
                // Usando lo que tenemos en windows.globales del index hacemos el cambio de formato
                let lastDate = DateTime.fromISO(lastStatus.dateTime).toFormat(window.formatDateTime.outputFormat)

                return `${finalStatus.value}  ${lastDate}`
              }
            },
          },
        ],
        responsiveLayout: false, // activado el scroll horizontal, también: ['hide'|true|false]
        initialSort: [
          // establecer el ordenamiento inicial de los datos
          { column: 'value', dir: 'asc' },
        ],
        columnDefaults: {
          tooltip: true, //show tool tips on cells
        },

        // mostrar al final de la tabla un botón para agregar registros
        footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
      })
      Boxes.#table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', Boxes.#addRow))
    } catch (e) {
      Toast.show({ title: 'Clients', message: 'Falló la carga de la información', mode: 'danger', error: e })
    }

    return this
  }

  static #editRowClick = async (e, cell) => {
    Boxes.#currentOption = 'edit'
    console.log(cell.getRow().getData())
    Boxes.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Update of Boxes</h5>`,
      content: Boxes.#form,
      buttons: [
        { caption: editButton, classes: 'btn btn-primary me-2', action: () => Boxes.#edit(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Boxes.#modal.close() },
      ],
      doSomething: idModal => Boxes.#displayDataOnForm(idModal, cell.getRow().getData()),
    })
    Boxes.#modal.show()
  }

  static #deleteRowClick = async (e, cell) => {
    Boxes.#currentOption = 'delete'
    console.log(cell.getRow().getData())
    Boxes.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Disposal of Boxes</h5>`,
      content: `<span class="text-back dark:text-gray-300">
                Confirm the elimination of the Box:<br>
                ${cell.getRow().getData().id} - ${cell.getRow().getData().content}<br>
                Adressee: ${cell.getRow().getData().addressee.name}<br>
                Sender: ${cell.getRow().getData().sender.name}<br>
              </span>`,
      buttons: [
        { caption: deleteButton, classes: 'btn btn-primary me-2', action: () => Boxes.#delete(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Boxes.#modal.close() },
      ],
    })
    Boxes.#modal.show()
  }

  static async #addRow() {
    Boxes.#currentOption = 'add'
    Boxes.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Boxes Entry</h5>`,
      content: Boxes.#form,
      buttons: [
        { caption: addButton, classes: 'btn btn-primary me-2', action: () => Boxes.#add() },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Boxes.#modal.close() },
      ],
      doSomething: Boxes.#displayDataOnForm,
    })
    Boxes.#modal.show()
  }

  static async #add() {
    try {
      // Verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!Helpers.okForm('#form-boxes', Boxes.#otherValidations)) {
        return
      }

      // Obtener del formulario el objeto con los datos que se envian a la solicitud POST
      const body = Boxes.#getFormData()

      // Enviar la solicitud de creación con los datos del formulario
      let response = await Helpers.fetchJSON(`${urlAPI}/box`, {
        method: 'POST',
        body,
      })

      if (response.message === 'ok') {
        Boxes.#table.addRow(response.data) // Agregar la caja a la tabla
        Boxes.#modal.remove()
        Toast.show({ message: 'Added succesfully' })
      } else {
        Toast.show({ message: 'Unable to add the box', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Failed to create the box', mode: 'danger', error: e })
    }
  }

  static async #edit(cell) {
    try {
      // Verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!Helpers.okForm('#form-boxes', Boxes.#otherValidations)) {
        return
      }

      // Obtener del formulario el objeto con los datos que se envian a la solicitud PATCH
      const body = Boxes.#getFormData()

      // Configurar la url para enviar para enviar la solicitud PATCH
      const url = `${urlAPI}/box/${cell.getRow().getData().id}`

      // Intentar enviar la solicitud de actualización
      let response = await Helpers.fetchJSON(url, {
        method: 'PATCH',
        body,
      })

      if (response.message === 'ok') {
        Toast.show({ message: 'Box updated successfully' })
        cell.getRow().update(response.data)
        Boxes.#modal.remove()
      } else {
        Toast.show({ message: 'The Box was unable to update ', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Problems with the update of the Box', mode: 'danger', error: e })
    }
  }

  static async #delete(cell) {
    try {
      const url = `${urlAPI}/box/${cell.getRow().getData().id}`
 
      // Enviar la solicitud de eliiniación
      let response = await Helpers.fetchJSON(url, {
        method: 'DELETE',
      })

      if (response.message === 'ok') {
        Toast.show({ message: 'Box deleted successfully' })
        cell.getRow().delete()
        Boxes.#modal.close()
      } else {
        Toast.show({ message: 'Unable to delete the Box', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Failed to delete the Box', mode: 'danger', error: e })
    }
  }

  static async #displayDataOnForm(idModal, rowData) {
    // Referenciar el select "sender"
    const selectSender = document.querySelector(`#${idModal} #sender`)
    // Asignar la lista de opciones al select "sotrer" de boxes.html
    selectSender.innerHTML = Boxes.#customers
    // Referenciar al select "addressee"
    const selectAddressee = document.querySelector(`#${idModal} #addressee`)
    // Asignar la lista de opciones al select "addressee" de boxes.html
    selectAddressee.innerHTML = Boxes.#customers
    console.log(rowData)

    // Mirar para el editar
    if (Boxes.#currentOption === 'edit') {
      // Mostrar los datos de la fila actual en el formulario
      document.querySelector(`#${idModal} #numGuide`).value = rowData.id
      document.querySelector(`#${idModal} #fragile`).checked = rowData.isFragile // checked para chequear una casilla
      document.querySelector(`#${idModal} #content`).value = rowData.content
      document.querySelector(`#${idModal} #height`).value = rowData.height
      document.querySelector(`#${idModal} #width`).value = rowData.width
      document.querySelector(`#${idModal} #lenght`).value = rowData.lenght
      document.querySelector(`#${idModal} #weight`).value = rowData.weight
      document.querySelector(`#${idModal} #value`).value = rowData.value
      // Para hallar el nombre del sender y addressee:
      selectSender.value = rowData.sender.id // Se pone id, no se el por qué
      selectAddressee.value = rowData.addressee.id
    }
  }

  static #getFormData() {
    // recuerde utilizar parseInt(), parseFloat() o Number() cuando sea necesario
    return {
      // id: document.querySelector(`#${Boxes.#modal.id} #id`).value,
      isFragile: document.querySelector(`#${Boxes.#modal.id} #fragile`).checked,
      sender: document.querySelector(`#${Boxes.#modal.id} #sender`).value,
      addressee: document.querySelector(`#${Boxes.#modal.id} #addressee`).value,
      content: document.querySelector(`#${Boxes.#modal.id} #content`).value,
      height: parseFloat(document.querySelector(`#${Boxes.#modal.id} #height`).value),
      width: parseFloat(document.querySelector(`#${Boxes.#modal.id} #width`).value),
      lenght: parseFloat(document.querySelector(`#${Boxes.#modal.id} #lenght`).value),
      weight: parseFloat(document.querySelector(`#${Boxes.#modal.id} #weight`).value),
      value: parseFloat(document.querySelector(`#${Boxes.#modal.id} #value`).value),
    }
  }

  static #otherValidations() {
    // Referencia de los elementos <select> sender y addressee
    // Referenciar el select "sender"
    let selectSender = document.querySelector(`#sender`)
    // Referenciar al select "addressee"
    let selectAddressee = document.querySelector(`#addressee`)
    let content = document.querySelector(`#content`)
    if (selectSender.value === '') {
      Toast.show({ message: 'You need to select a sender', mode: 'warning' })
      return false
    }
    if (selectAddressee.value === '') {
      Toast.show({ message: 'You need to select an addressee', mode: 'warning' })
      return false
    }
    if (selectAddressee.value === selectSender.value) {
      Toast.show({ message: 'The sender and the addressee cannot be the same ', mode: 'warning' })
      return false
    }
    if (content === '') {
      Toast.show({ message: 'There must be a content described', mode: 'warning' })
      return false
    }

    return true
  }

  
}
