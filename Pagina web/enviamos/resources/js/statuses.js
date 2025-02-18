export default class Statuses {
  static #form
  static #addForm // Es el formato del addRow
  static #table
  static #currentOption
  static #typeStatus // Es usado para el toOptionList del add
  static #humanStatus // Es un atributo global para el DeliveryStatus en formato humano
  static #numGuide
  static #deliveryType
  static #modal
  static #data

  constructor() {
    throw new Error('No instances are required, all methods are static. Use Statuses.init()')
  }

  static async init() {
    try {
      // Cargar HTML que contiene el formulario
      Statuses.#form = await Helpers.fetchText('./resources/html/statuses.html')
      document.querySelector('main').innerHTML = Statuses.#form

      // Cargar HTML Que contiene el add
      Statuses.#addForm = await Helpers.fetchText('./resources/html/statusesForm.html')

      // Cargar el endpoint de DeliveryStatus en formato Humano
      const statusResponse = await Helpers.fetchJSON(`${urlAPI}/delivery/status`)
      if (statusResponse.message != 'ok') {
        throw new Error(statusResponse.message)
      }
      // Tenemos que pasar ese mensaje pues solo necesitamos la data que es el array, y lo definimos asi global porque lo utilizo en varias parte
      Statuses.#humanStatus = statusResponse.data

      // Crear las opciones para un select de typeStatus
      Statuses.#typeStatus = Helpers.toOptionList({
        items: Statuses.#humanStatus,
        value: 'key',
        text: 'value', // Se revisa de la petición, el key es como el nombre, y el text pongo el value que estoy buscando
        firstOption: 'Choose a Delivery Status',
      })

      // Mostrar el la fecha actualizada por segundo
      Statuses.#intervalTime()

      // Hacer un reset del html cada vez que haya un cambio en el tipo de envio
      document.querySelector('#deliveryType').addEventListener('change', Statuses.#reset)

      // Button Search Delivery
      document.querySelector('#button').addEventListener('click', function (e) {
        return Statuses.loadData(e)
      })
    } catch (e) {
      Toast.show({ title: 'Statuses', message: 'Problems with the loading of the data', mode: 'danger', error: e })
    }
    return this
  }

  // ############################# Funciones relacionadas a la tabla ###################################

  static #deleteRowClick = async (e, cell) => {
    Statuses.#currentOption = 'delete'
    // console.log(cell.getRow().getData())

    Statuses.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>Status elimination</h5>`,
      content: `<span class="text-back dark:text-gray-300">
                Confirm the elimination of the Status:<br>
                ${Statuses.#getStatus(cell)} - ${Statuses.#humanTime(cell)}<br>
              </span>`,
      buttons: [
        { caption: deleteButton, classes: 'btn btn-primary me-2', action: () => Statuses.#delete(cell) },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Statuses.#modal.close() },
      ],
    })
    Statuses.#modal.show()
  }

  static async #addRow() {
    Statuses.#currentOption = 'add'

    Statuses.#modal = new Modal({
      classes: 'col-12 col-sm-10 col-md-9 col-lg-8 col-xl-7',
      title: `<h5>DeliveryType entry</h5>`,
      content: Statuses.#addForm,
      buttons: [
        { caption: addButton, classes: 'btn btn-primary me-2', action: () => Statuses.#add() },
        { caption: cancelButton, classes: 'btn btn-secondary', action: () => Statuses.#modal.close() },
      ],
      doSomething: Statuses.#displayDataOnForm,
    })
    Statuses.#modal.show()
  }

  static async #add() {
    try {
      // Verificar si los datos cumplen con las restricciones indicadas en el formulario HTML
      if (!Helpers.okForm('#form-add-Status')) {
        // console.log('Fui atrapado en el helpers')
        return
      }

      Statuses.#updateData()
      // console.log('Funcione')
    } catch (e) {
      Toast.show({ message: 'Failed record creation operation', mode: 'danger', error: e })
    }
  }

  static async #delete(cell) {
    try {
      // Mandamos la peticion de eliminación
      Statuses.#updateData()
    } catch (e) {
      Toast.show({ message: 'Failed to delete the status', mode: 'danger', error: e })
    }
  }

  static async #updateData() {
    // Obtener del formulario el objeto con los datos que se envian a la solicitud POST
    const body = Statuses.#getFormData()
    // console.log(body)

    // Enviar la solicitud de creación con los datos del formulario
    let response = await Helpers.fetchJSON(`${urlAPI}/${Statuses.#deliveryType}/${Statuses.#numGuide}`, {
      method: 'PATCH',
      body,
    })
    if (response.message === 'ok') {
      Statuses.#table.replaceData(response.data.status) // Agregar la mercancia a la tabla
      Statuses.#modal.remove()
      if (Statuses.#currentOption === 'add') {
        Toast.show({ message: 'Added successfully' })
      } else {
        Toast.show({ message: 'Deleted successfully' })
      }
    } else {
      Toast.show({ message: response.message, mode: 'danger', error: response })
    }
  }

  static #displayDataOnForm(idModal) {
    // referenciar el select "client"
    const selectStatus = document.querySelector(`#${idModal} #deliveryStatus`) // # antes del {idModal} porque es un atributo que estamos referenciando del html
    // asignar la lista de opciones al select "client" de merchandises.html
    selectStatus.innerHTML = Statuses.#typeStatus
    // por defecto, asignar a ingreso del formulario la fecha y hora actual
    let now = DateTime.now()
    // console.log(now)

    // console.log(document.querySelector('#form-add-Status #dateTime'))

    document.querySelector(`#${idModal} #dateTime`).value = now.toFormat('yyyy-MM-dd HH:mm') // Por que no se puede usar el
    // formatDateTime.outputFormat del index???
  }

  /** En este primero se saca la información de la tabla, y dependiendo de la opción actual
   *  Añade un nuevo estado, o elimina uno
   *
   * @returns Un JSONObject que es el cuerpo de la petición
   */
  static #getFormData() {
    let body = Statuses.#table.getData()

    // Si el estado actual es add
    if (Statuses.#currentOption === 'add') {
      const newBody = {
        dateTime: document.querySelector(`#${Statuses.#modal.id} #dateTime`).value,
        deliveryStatus: document.querySelector(`#${Statuses.#modal.id} #deliveryStatus`).value,
      }

      // Verificación de las horas
      if (Statuses.#otherValidations()) {
        // console.log(newBody)
        body.push(newBody)
      }
    }
    // Si el estado es delete
    else if (Statuses.#currentOption === 'delete' && Statuses.#statusValidationsToDelete(body)) {
      body.pop() // The method to remove the last element of the array
    }

    // console.log(body)
    // Devolvemos un JSONObject para la petición, pues en el backend trabajamos con JSONObject
    return { status: body }
  }

  /** En esta función verificamos si la hora que esta ingresando el usuario cumple con que tiene maximo una hora despues de la actual
   *  O minimo una antes de la actual
   *
   * @returns Verdadero si cumple con las validaciones, falso si no
   */
  static #otherValidations() {
    // Establecer limites de fecha
    const time = DateTime.now()

    // Valor mayor
    const max = time.plus({ hours: 1 })

    // Valor menor
    const min = time.minus({ hours: 1 })

    // Necesitamos pedir el userTime, y pasarlo a la zona local, pues la que teniamos allá no contaba con todos los valores
    const userTime = DateTime.fromISO(document.querySelector('#form-add-Status #dateTime').value, { zone: 'local' })

    // Verificación de que si la hora que dio el usuario es mayor al maximo, o menor al minimo
    if (userTime.valueOf() > max.valueOf() || userTime.valueOf() < min.valueOf()) {
      console.log('Current time exceeds max:', userTime > max)
      console.log('Current time is less than min:', userTime < min)
      Toast.show({ message: "Error, you can't have a status an hour after or before the last one.", mode: 'danger' })
      return false
    }

    return true
  }

  /**
   *
   * @param {*} body Todo el array de la tabla
   * @returns Verdadero si se puede eliminar, falso si no
   */
  static #statusValidationsToDelete(body) {
    // Verificar el ultimo estado
    const element = body.at(-1) // el .at(-1) para acceder ultiomo elemento
    // console.log(element)

    // Conseguir el deliveryStatus del ultimo estado
    let lastElement = element.deliveryStatus
    // console.log(lastElement)

    // Hace la verificación de que no sea uno de estos 3, sin embargo solo va a funcionar el RECEIVED porque es lo que dice los lineamientos
    if (!(lastElement === 'RECEIVED' || lastElement === 'DELIVERED' || lastElement === 'RETURNED')) {
      //(!(lastElement === 'RECEIVED' || lastElement === 'DELIVERED' || lastElement === 'RETURNED'))
      return true
    } else {
      Toast.show({ message: `You cannot delete ${lastElement}`, mode: 'danger' })
      return false
    }
  }

  // ############################### Funciones Propias ####################################

  /**
   * En esta función se hace el intervalo de tiempo, entonces aqui se genera el intervalo y se termina
   */
  static #intervalTime() {
    // Se inicia el intervalo
    setInterval(Statuses.#time, 1000)

    // Verificación de si se sigue en la misma pagina, para que en caso de que no, se termine el intervalo
    if (document.querySelector('#dateTime') === null) {
      clearInterval()
    }
  }

  /**
   * En esta función, se consigue la hora actual, y se manda al formato buscado
   */
  static #time() {
    // Hora actual
    const now = DateTime.now()

    // Verificación de que la pagina si este cargada en la parte de la fecha
    if (!(document.querySelector('#dateTime') === null)) {
      document.querySelector('#dateTime').value = now.toFormat('dd/MM/yyyy, HH:mm:ss a')
    }
  }

  static #humanTime(cell) {
    let time = cell.getRow().getData().dateTime
    const humanTime = DateTime.fromISO(time).toFormat('D, tt')
    return humanTime
  }

  /**
   * Esta función lo que hace, es que vuelve al estado inicial del Card definido en el HTML
   */
  static #reset() {
    const html = document.getElementById('initial-text')
    html.innerHTML = `Select a delivery Type, give a num Guide and click on Search Delivery`
    // document.querySelector('#numGuide').value = ''
  }

  /** Dependiendo el caso por ejemplo cuando lo llamo en delete me da la fila,
   *  y cuando lo llamo desde la tabla me da la celda.
   *
   * @param {*} row Es la información de la celda o fila
   * @returns El estado den formato humano
   */
  static #getStatus(row) {
    let status = row.getData().deliveryStatus // getValue() para el valor de la celda, getData() para el valor de la fila
    // console.log(status)

    let actualStatus = Statuses.#humanStatus.find(current => status === current.key) // Encuentra el valor actual, con el del formato humano
    // console.log(actualStatus)
    // console.log(actualStatus.value)

    return `${actualStatus.value}` // Se devuelve .value porque actualState es un array, y la llave que necesitamos es el value
  }

  /** En esta función se hace todo el cargado y se muestra toda la información, ya estando en la tabla y en el card.
   *
   * @param {*} e Es como la cosa que da el botón al oprimirlo, entonces es utilizado para prevenir el default
   * @returns Toda la información de la tabla, del envio
   */
  static async loadData(e) {
    try {
      e.preventDefault()
      // Necesario para que las restricciones del HTML funcionen
      if (!Helpers.okForm('#form-statuses')) {
        return
      }

      // ####################   Sacamos la información del form    #####################
      // Del tipo de envio
      Statuses.#deliveryType = document.querySelector('#deliveryType').value

      // Se verifica si no se ha escogido algun tipo de envio
      if (Statuses.#deliveryType === 'null') {
        // No se si se pueda poner que como eso equivale null daria falso
        Toast.show({ message: 'You need to select a delivery type', mode: 'warning' })
        return
      }

      // Del numero de Guia
      Statuses.#numGuide = document.querySelector('#numGuide').value

      // #############3 Acceder a la información del deliveryStatus  #######################

      // Hacemos la petición
      let response = await Helpers.fetchJSON(`${urlAPI}/${Statuses.#deliveryType}/id/${Statuses.#numGuide}`)
      if (response.message != 'ok') {
        // Lanzamos un mensaje de error, en el formato estipulado
        const error = document.getElementById('initial-text')
        error.innerHTML = `<div>
                            <h5>Delivery Info</h5>
                            <div class="alert alert-dismissible alert-warning">
                              <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                              <h5 class="alert-heading">Failed Search</h5>
                              <p class="mb-0">${response.message} </a>.</p>
                            </div>
                            </div>`
        throw new Error(response.message)
      }
      // Sacamos información del response
      const data = (Statuses.#data = response.data)

      // Definimos el html para insertar y cambiarlo:  document.getElementById("id")
      const html = document.getElementById('initial-text')
      html.innerHTML = `<div>
                         <h5>Delivery Info</h5>
                         <span class="text-back dark:text-gray-300">
                         Sender: ${data.sender.name} - ${data.sender.address} - ${data.sender.city}<br>
                         Adressee: ${data.addressee.name} - ${data.addressee.address} - ${data.addressee.city}<br>
                         Content: ${data.content}<br>
                         Price: ${data.payment}<br>           
                         </span> 
                       </div>
                       <div id="table-container" class="m-2"></div>` // PREGUNTAR SI ES VALUE O PAYMENT

      // ############################## Creación de la tabla ##########################################
      Statuses.#table = new Tabulator('#table-container', {
        height: 'auto',
        data: data.status, // Se pone status, porque se tiene que definir que parte de la información necesitamos
        layout: 'fitData',
        columns: [
          { formatter: Statuses.#deleteRowButton, width: 50, hozAlign: 'center', cellClick: Statuses.#deleteRowClick },
          {
            title: 'Date and Time',
            field: 'dateTime',
            width: 400,
            formatter: function (cell) {
              // Hallamos valor de la celda
              let time = cell.getValue()
              // Creamos el nuevo formato de la fecha
              const newFormat = "hh:mm a, cccc d 'of' LLLL 'from' kkkk"
              let lastDate = DateTime.fromISO(time).toFormat(newFormat) // Una posible opción es usar lo que ofrece Luxon, y tomar el
              // toLocaleString(DateTime.DATETIME_HUGE) para obtener la fecha en un formato parecido

              return `${lastDate}`
            },
          },
          {
            title: 'Status',
            field: 'deliveryStatus',
            hozAlign: 'center',
            width: 800,
            formatter: function (cell) {
              // Toca definirlo asi porque es dinamico
              return Statuses.#getStatus(cell)
            },
          },
        ],
        responsiveLayout: false,
        initialSort: [{ column: 'dateTime', dir: 'asc' }],
        columnDefaults: {
          tooltip: true,
        },

        // mostrar al final de la tabla un botón para agregar nuevos estados
        footerElement: `<div class='container-fluid d-flex justify-content-end p-0'>${addRowButton}</div>`,
      })

      // Botón Nuevo Registro
      Statuses.#table.on('tableBuilt', () => document.querySelector('#add-row').addEventListener('click', Statuses.#addRow))
      // Cargar la información  Se puede hacer asi o como lo estoy haciendo
      // document.querySelector('main').insertAdjacentHTML('beforeend', html)
    } catch (e) {
      Toast.show({ message: 'Problems with the loading', mode: 'danger', error: e })
    }
  }

  static #deleteRowButton = cell => {
    // sin revisar el comportamiento cuando se elimina/agrega un estado
    const rowIndex = cell.getRow().getIndex()
    const lastIndex = Statuses.#data.status[Statuses.#data.status.length - 1].id
    return rowIndex === lastIndex ? `<button id="delete-row" class="border-0 bg-transparent" data-bs-toggle="tooltip" title="Eliminar">${icons.delete}</button>` : '&#128528;'   // Jajajajajajaja 

  }
}
