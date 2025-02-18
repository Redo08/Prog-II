export default class Home {
  static #form

  constructor() {
    throw new Error('Does not require instances, all methods are static, use Home.init()')
  }

  static async init() {
    try {
        Home.#form = await Helpers.fetchText('./resources/html/home.html')
        document.querySelector('main').innerHTML = Home.#form
    } catch (e) {
        Toast.show({ title: 'Home', message: 'Problems with loading the data', mode: 'danger', error: e })
    }
  }
}
