export default class About {
  static #form

  constructor() {
    throw new Error('Does not require instances, all methods are static, use About.init()')
  }

  static async init() {
    try {
      About.#form = await Helpers.fetchText('./resources/html/resume.html')
      document.querySelector('main').innerHTML = About.#form
      // .innerHTML : Reemplaza el contenido de la capa
      // .insertBeforeEnd : Agregar antes del final
    } catch (e) {
      Toast.show({ title: 'Resume', message: 'Problems with loading the data', mode: 'danger', error: e })
    }
  }
}
