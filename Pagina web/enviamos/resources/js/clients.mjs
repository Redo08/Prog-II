export default class Clients {
  static #table
  static #modal
  static #currentOption
  static #form
  static #cities

  constructor() {
    throw new Error('No requiere instancias, todos los métodos son estáticos. Use Clients.init()')
  }

  static async init() {
    try {
      // Crear las opciones para un select de ciudades
      Clients.#cities = Helpers.toOptionList({
        items: await Helpers.fetchJSON('./resources/assets/ciudades.json'),
        value: 'codigo',
        text: 'nombre',
        firstOption: 'Choose a city',
      })

      Clients.#form = await Helpers.fetchText('./resources/html/clients.html')

      // intentar cargar los datos de los usuarios
      const response = await Helpers.fetchJSON(`${urlAPI}/client`) // Alt 96 ``
      if (response.message != 'ok') {
        throw new Error(response.message)
      }

      // Agregar al <main> de index.html la capa que contendra la tabla
      document.querySelector('main').innerHTML = `<div id="table-container2" class="m-2"></div>`

      Clients.#table = new Tabulator('#table-container2', {
        height: tableHeight, // establecer la altura para habilitar el DOM virtual y mejorar la velocidad de procesamiento
        data: response.data,
        layout: 'fitDataStretch', // ajustar columnas al ancho disponible. También fitData|fitDataFill|fitDataStretch|fitDataTable|fitColumns
        columns: [
          // definir las columnas de la tabla, para tipos datetime se utiliza formatDateTime definido en index.mjs
          { formatter: editRowButton, width: 40, hozAlign: 'center', cellClick: Clients.#editRowClick },
          { formatter: deleteRowButton, width: 40, hozAlign: 'center', cellClick: Clients.#deleteRowClick },
          { title: 'Id', field: 'id', hozAlign: 'center', width: 90 },
          { title: 'Name', field: 'name', width: 200 },
          { title: 'Address', field: 'address', width: 300 },
          { title: 'PhoneNumber', field: 'phoneNumber', hozAlign: 'center', width: 200 },
          { title: 'City', field: 'city', width: 280 },
        ],
        responsiveLayout: false, // activado el scroll horizontal, también: ['hide'|true|false]
        initialSort: [
          // establecer el ordenamiento inicial de los datos
          { column: 'name', dir: 'asc' },
        ],
        columnDefaults: {
          tooltip: true, //show tool tips on cells
        },

        // mostrar al final de la tabla un botón para agregar registros
        footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
      })
      // Boton Nuevo registro
      Clients.#table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', Clients.#addRow))
    } catch (e) {
      Toast.show({ title: 'Clients', message: 'Falló la carga de la información', mode: 'danger', error: e })
    }

    return this
  }

  static async #addRow() {
    Clients.#currentOption = 'add'

    Clients.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Creation of clients</h5>`,
      content: Clients.#form,
      buttons: [
        { caption: addButton, classes: 'btn btn-primary me-2', action: () => Clients.#add() },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Clients.#modal.close() },
      ],
      doSomething: Clients.#displayDataOnForm,
    })
    Clients.#modal.show()
  }

  static async #add() {
    try {
      // Verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!(Helpers.okForm('#form-clients'))) {
        return
      }

      // Obtener del formulario el objeto con los datos que se envian a la solicitud POST
      const body = Clients.#getFormData()

      // Enviar la solicitud de creación con los datos del formulario
      let response = await Helpers.fetchJSON(`${urlAPI}/client`, {
        method: 'POST',
        body,
      })

      if (response.message === 'ok') {
        Clients.#table.addRow(response.data) // Agregar el cliente a la tabla
        Clients.#modal.remove()
        Toast.show({ message: 'Added succesfully' })
      } else {
        Toast.show({ message: 'Unable to add the client', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Failed to create the client', mode: 'danger', error: e })
    }
  }

  static #editRowClick = async (e, cell) => {
    Clients.#currentOption = 'edit'
    console.log(cell.getRow().getData())
    Clients.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Update of Clients</h5>`,
      content: Clients.#form,
      buttons: [
        { caption: editButton, classes: 'btn btn-primary me-2', action: () => Clients.#edit(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Clients.#modal.close() },
      ],
      doSomething: idModal => Clients.#displayDataOnForm(idModal, cell.getRow().getData()),
    })
    Clients.#modal.show()
  }

  static async #edit(cell) {
    try {
      // Verificar si los datos cumplen con los estandares del HTML
      if (!(Helpers.okForm('#form-clients'))) {
        return
      }

      // Obtener del formulario el objeto con los datos que se va a enviar a la solicitud PATCH
      const body = Clients.#getFormData()

      // Configurar la url para enviar la solicitud
      let url = `${urlAPI}/client/${cell.getRow().getData().id}`

      // Intentar enviar la solicitud de actualización
      let response = await Helpers.fetchJSON(url, {
        method: 'PATCH',
        body,
      })

      if (response.message === 'ok') {
        Toast.show({ message: 'Client updated successfully' })
        cell.getRow().update(response.data)
        Clients.#modal.close()
      } else {
        Toast.show({ message: 'Unable to update the client', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Failed to update the client', mode: 'danger', error: e })
    }
  }

  static #deleteRowClick = async (e, cell) => {
    Clients.#currentOption = 'delete'
    console.log(cell.getRow().getData())
    Clients.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Elimination of Clients</h5>`,
      content: `<span class="text-back dark:text-gray-300">
                Confirm the elimination of the client:<br>
                ID - ${cell.getRow().getData().id}<br>
                Name: ${cell.getRow().getData().name}<br>
                Address: ${cell.getRow().getData().address}<br>
                PhoneNumber: ${cell.getRow().getData().phoneNumber}<br>
                City: ${cell.getRow().getData().city}<br>
              </span>`,
      buttons: [
        { caption: deleteButton, classes: 'btn btn-primary me-2', action: () => Clients.#delete(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Clients.#modal.close() },
      ],
    })
    Clients.#modal.show()
  }

  static async #delete(cell) {
    try {
      const url = `${urlAPI}/client/${cell.getRow().getData().id}`

      // Enviar la solicitud de eliiniación
      let response = await Helpers.fetchJSON(url, {
        method: 'DELETE',
      })

      if (response.message === 'ok') {
        Toast.show({ message: 'Client deleted successfully' })
        cell.getRow().delete()
        Clients.#modal.close()
      } else {
        Toast.show({ message: 'Unable to delete the client', mode: 'danger', error: response })
      }
    } catch (e) {
      Toast.show({ message: 'Failed to delete the client', mode: 'danger', error: e })
    }
  }

  static #displayDataOnForm(idModal, rowData) {
    // Referenciar al select ciudad
    const selectCities = document.querySelector(`#${idModal} #city`)
    // Asignar la lista de opciones al select city dentro de clients.html
    selectCities.innerHTML = Clients.#cities

    if (Clients.#currentOption === 'edit') {
      // Mostrar los datos del actual
      document.querySelector(`#${idModal} #id`).value = rowData.id
      document.querySelector(`#${idModal} #name`).value = rowData.name
      document.querySelector(`#${idModal} #address`).value = rowData.address
      document.querySelector(`#${idModal} #phoneNumber`).value = rowData.phoneNumber
      // Buscar el indice de la opción cuyo texto sea igual al de la ciudad de la fila seleccionada
      Helpers.selectOptionByText(selectCities, rowData.city)
    }
  }

  /**
   * Recupera los datos del formulario y crea un objeto para ser retornado
   * @returns Un objeto con los datos del usuario
   */
  static #getFormData() {
    const cities = document.querySelector(`#${Clients.#modal.id} #city`)
    const index = cities.selectedIndex
    return {
      id: document.querySelector(`#${Clients.#modal.id} #id`).value,
      name: document.querySelector(`#${Clients.#modal.id} #name`).value,
      address: document.querySelector(`#${Clients.#modal.id} #address`).value,
      phoneNumber: document.querySelector(`#${Clients.#modal.id} #phoneNumber`).value,
      city: cities.options[index].text,
    }
  }

  static #otherValidations() {   // Falta implementar mejor el helpers para poder sacar los TOAST bien
    // Referencia de los elementos <select> sender y addressee
    // Referenciar el select "sender"
    let name = document.querySelector(`#name`)
    // Referenciar al select "addressee"
    let address = document.querySelector(`#address`)
    if (!name.value.trim()) {
      Toast.show({ message: 'You need to give a name', mode: 'warning' })
      return false
    }
    if (address.value === '') {
      Toast.show({ message: 'You need to give an address', mode: 'warning' })
      return false
    }

    return true
  }
}
