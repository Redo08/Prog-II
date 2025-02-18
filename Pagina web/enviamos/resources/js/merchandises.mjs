export default class Merchandises {
  static #table
  static #modal
  static #currentOption
  static #form
  static #customers // El # es señar de privado
  constructor() {
    throw new Error('No instances required, All methods are static. Use Merchandises.init()')
  }

  static async init() {
    try {
      Merchandises.#form = await Helpers.fetchText('./resources/html/merchandises.html')

      // intentar cargar los datos de los usuarios
      let response = await Helpers.fetchJSON(`${urlAPI}/client`)
      if (response.message != 'ok') {
        throw new Exception(response)
      }

      // Crear las opciones para un select de clientes
      Merchandises.#customers = Helpers.toOptionList({
        items: response.data,
        value: 'id',
        text: 'name',
        firstOption: 'Choose a client',
      })

      response = await Helpers.fetchJSON(`${urlAPI}/merchandise`)
      if (response.message != 'ok') {
        throw new Error(response.message)
      }

      // agregar al <main> de index.html la capa que contendrá la tabla
      document.querySelector('main').innerHTML = `<div id="table-container" class="m-2"></div>`

      Merchandises.#table = new Tabulator('#table-container', {
        height: 'auto', // establecer la altura para habilitar el DOM virtual y mejorar la velocidad de procesamiento
        data: response.data,
        layout: 'fitData', // ajustar columnas al ancho disponible. También fitData|fitDataFill|fitDataStretch|fitDataTable|fitColumns
        columns: [
          // definir las columnas de la tabla, para tipos datetime se utiliza formatDateTime definido en index.mjs
          { formatter: editRowButton, width: 40, hozAlign: 'center', cellClick: Merchandises.#editRowClick },
          { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: Merchandises.#deleteRowClick },
          { title: 'Id', field: 'id', hozAlign: 'center', width: 90 },
          { title: 'Client', field: 'storer.name', width: 200 },
          { title: 'Content', field: 'content', width: 300 },
          { title: 'Arrival', field: 'dateOfArrival', width: 150, formatter: 'datetime', formatterParams: formatDateTime },
          { title: 'Departure', field: 'dateOfDeparture', width: 150, formatter: 'datetime', formatterParams: formatDateTime },
          { title: 'Days', field: 'days', hozAlign: 'center', width: 65 },
          { title: 'Height', field: 'volume', hozAlign: 'center', visible: false },
          { title: 'Width', field: 'volume', hozAlign: 'center', visible: false },
          { title: 'Lenght', field: 'volume', hozAlign: 'center', visible: false },
          { title: 'Vol. m³', field: 'volume', hozAlign: 'center', width: 80 },
          { title: 'Price', field: 'payment', hozAlign: 'right', width: 100, formatter: 'money' },
          { title: 'Warehouse', field: 'warehouse', width: 280 },
        ],
        responsiveLayout: false, // activado el scroll horizontal, también: ['hide'|true|false]
        initialSort: [
          // establecer el ordenamiento inicial de los datos
          { column: 'dateOfArrival', dir: 'asc' },
        ],
        columnDefaults: {
          tooltip: true, //show tool tips on cells
        },

        // mostrar al final de la tabla un botón para agregar registros
        footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
      })

      // Boton Nuevo registro
      Merchandises.#table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', Merchandises.#addRow))
    } catch (e) {
      Toast.show({ title: 'Merchandises', message: 'Falló la carga de la información', mode: 'danger', error: e })
    }

    return this
  }

  static #editRowClick = async (e, cell) => {
    Merchandises.#currentOption = 'edit'
    // console.log(cell.getRow().getData())

    Merchandises.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Update of merchandise</h5>`,
      content: Merchandises.#form,
      buttons: [
        { caption: editButton, classes: 'btn btn-primary me-2', action: () => Merchandises.#edit(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Merchandises.#modal.close() },
      ],
      doSomething: idModal => Merchandises.#displayDataOnForm(idModal, cell.getRow().getData()),
    })
    Merchandises.#modal.show()
  }

  static #deleteRowClick = async (e, cell) => {
    Merchandises.#currentOption = 'delete'
    // console.log(cell.getRow().getData())
    Merchandises.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Disposal of merchandise</h5>`,
      content: `<span class="text-back dark:text-gray-300">
                  Confirm the elimination of the merchandise:<br>
                  ${cell.getRow().getData().id} - ${cell.getRow().getData().content}<br>
                  Warehouse: ${cell.getRow().getData().warehouse}<br>
                  Owner: ${cell.getRow().getData().storer.name}<br>
                </span>`,
      buttons: [
        { caption: deleteButton, classes: 'btn btn-primary me-2', action: () => Merchandises.#delete(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Merchandises.#modal.close() },
      ],
    })
    Merchandises.#modal.show()
  }

  static async #addRow() {
    Merchandises.#currentOption = 'add'

    Merchandises.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Merchandise entry</h5>`,
      content: Merchandises.#form,
      buttons: [
        { caption: addButton, classes: 'btn btn-primary me-2', action: () => Merchandises.#add() },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Merchandises.#modal.close()},
      ],
      doSomething: Merchandises.#displayDataOnForm,
    })
    Merchandises.#modal.show()
  }


  static async #add() {
    try {
      // Verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!Helpers.okForm('#form-merchandises', Merchandises.#otherValidations)) {
        return
      }

      // Obtener del formulario el objeto con los datos que se envian a la solicitud POST
      const body = Merchandises.#getFormData()

      // Enviar la solicitud de creación con los datos del formulario
      let response = await Helpers.fetchJSON(`${urlAPI}/merchandise`, {
        method: 'POST',
        body: body,
      })

      if (response.message === 'ok') {
        Merchandises.#table.addRow(response.data) // Agregar la mercancia a la tabla
        Merchandises.#modal.remove()
        Toast.show({ message: 'Added successfully' })
      } else {
        Toast.show({ message: 'Unable to add the record', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Failed record creation operation', mode: 'danger', error: e })
    }
  }

  static async #edit(cell) {
    try {
      // Verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!Helpers.okForm('#form-merchandises', Merchandises.#otherValidations)) {
        return
      }

      // Obtener del formulario el objeto con los datos que se envian a la solicitud PATCH
      const body = Merchandises.#getFormData()

      // Configurar la url para enviar para enviar la solicitud PATCH
      const url = `${urlAPI}/merchandise/${cell.getRow().getData().id}`

      // Intentar enviar la solicitud de actualización
      let response = await Helpers.fetchJSON(url, {
        method: 'PATCH',
        body,
      })

      if (response.message === 'ok') {
        Toast.show({ message: 'Merchandise updated successfully' })
        cell.getRow().update(response.data)
        Merchandises.#modal.remove()
      } else {
        Toast.show({ message: 'The merchandise was unable to update ', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Problems with the update of the merchandise', mode: 'danger', error: e })
    }
  }

  static async #delete(cell) {
    try {
      // Configurar la url para enviar la solicitud DELETE
      const url = `${urlAPI}/merchandise/${cell.getRow().getData().id}`

      // Enviar la solicitud de eliminación
      let response = await Helpers.fetchJSON(url, {
        method: 'DELETE',
      })

      if (response.message === 'ok') {
        Toast.show({ message: 'Merchandise deleted successfully' })
        // Eliminación
        cell.getRow().delete()
        Merchandises.#modal.close()
      } else {
        Toast.show({ message: 'The merchandise was unable to be deleted', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Problems with the elimination of the merchandise', mode: 'danger', error: e })
    }
  }

  static #displayDataOnForm(idModal, rowData) {
      // referenciar el select "client"
      const selectCustomers = document.querySelector(`#${idModal} #client`) // # antes del {idModal} porque es un atributo que estamos referenciando del html
      // asignar la lista de opciones al select "client" de merchandises.html
      selectCustomers.innerHTML = Merchandises.#customers

    // Si se piensa editar, cargamos los datos directamente del rowData
    if (Merchandises.#currentOption === 'edit') {
      // Mostrar los datos de la fila actual en el formulario
      document.querySelector(`#${idModal} #id`).value = rowData.id
      document.querySelector(`#${idModal} #content`).value = rowData.content
      document.querySelector(`#${idModal} #height`).value = rowData.height
      document.querySelector(`#${idModal} #width`).value = rowData.width
      document.querySelector(`#${idModal} #lenght`).value = rowData.lenght
      document.querySelector(`#${idModal} #entry`).value = rowData.dateOfArrival
      document.querySelector(`#${idModal} #departure`).value = rowData.dateOfDeparture
      document.querySelector(`#${idModal} #warehouse`).value = rowData.warehouse
      // Para hallar el nombre del cliente:
      selectCustomers.value = rowData.storer.id // Ya se porque, lo estamos sacando del #Customers
    } else {
      // por defecto, asignar a ingreso del formulario la fecha y hora actual
      let now = DateTime.now()
      console.log(now);
      
      console.log(document.querySelector('#form-merchandises #entry'));
      
      document.querySelector(`#${idModal} #entry`).value = now.toFormat('yyyy-MM-dd HH:mm') // Por que no se puede usar el
      // por defecto, asignar a la salida la hora del ingreso más una hora                                             formatDateTime.outputFormat del index???
      document.querySelector(`#${idModal} #departure`).value = now.plus({ hours: 1 }).toFormat('yyyy-MM-dd HH:mm') // También se puede plus minutes, seconds, days, etc.
    }

    // asignar la lista de opciones al select "client" de merchandises.html
  }
  static #getFormData() {
    // recuerde utilizar parseInt(), parseFloat() o Number() cuando sea necesario
    return {
      id: document.querySelector(`#${Merchandises.#modal.id} #id`).value,
      storer: document.querySelector(`#${Merchandises.#modal.id} #client`).value,
      content: document.querySelector(`#${Merchandises.#modal.id} #content`).value,
      height: parseFloat(document.querySelector(`#${Merchandises.#modal.id} #height`).value),
      width: parseFloat(document.querySelector(`#${Merchandises.#modal.id} #width`).value),
      lenght: parseFloat(document.querySelector(`#${Merchandises.#modal.id} #lenght`).value),
      dateOfArrival: document.querySelector(`#${Merchandises.#modal.id} #entry`).value,
      dateOfDeparture: document.querySelector(`#${Merchandises.#modal.id} #departure`).value,
      warehouse: document.querySelector(`#${Merchandises.#modal.id} #warehouse`).value,
    }
  }
  static #otherValidations() {
    // Referencia de los elementos <select> sender y addressee
    // Referenciar el select "sender"
    let selectStorer = document.querySelector(`#client`)
    let entry = document.querySelector('#entry')
    let departure = document.querySelector('#departure')
    
    // Necesita seleccionar un storer
    if (selectStorer.value === '') {
      Toast.show({ message: 'You need to select a storer', mode: 'warning' })
      return false
    } 

    // No se puede ingresar una fecha de salida antes que al de entrada
    if (entry.value > departure.value) {
      Toast.show({ message: 'You cannot select a date of departure before the date of entry', mode: 'danger' })
      return false
    }

    return true
  }
}
