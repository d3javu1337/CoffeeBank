import {makeAutoObservable} from "mobx";
import {ClientCompact} from "../dto/model/client/ClientCompactDto";
import AuthService from "../service/AuthService";
import axios, {HttpStatusCode} from "axios";
import {api_url} from "../http";

export default class Store {

    client = {} as ClientCompact
    isAuth = false
    isLoading = false

    constructor() { makeAutoObservable(this) }

    setClient(client: ClientCompact) {
        this.client = client
    }

    setIsAuth(isAuth: boolean) {
        this.isAuth = isAuth
    }

    setIsLoading(isLoading: boolean) {
        this.isLoading = isLoading
    }

    async login(email: string, password: string) {
        this.setIsLoading(true)
        try {
            const response = await AuthService.login(email, password);
            console.log(response.data);
            localStorage.setItem('token', response.data)
            this.setIsAuth(true)
        } catch (e) {
            console.error(e)
        } finally {
            this.setIsLoading(false)
        }
    }

    async registration(surname: string, name: string, patronymic: string, dateOfBirth: string,
                       phoneNumber: string, email: string, password: string) {
        this.setIsLoading(true)
        try {
            const response =
                await AuthService.registration(surname, name, patronymic, new Date(dateOfBirth), phoneNumber, email, password)
            console.log(response.data);
            if(response.status === HttpStatusCode.Accepted) {
                await this.login(email, password);
            }
        } catch (e) {
            console.error(e)
        } finally {
            this.setIsLoading(false)
        }
    }

    async checkAuth() {
        this.setIsLoading(true)
        try {
            const response = await axios.get<string>(`${api_url}/auth/refresh`, {withCredentials: true});
            console.log(response.data);
            localStorage.setItem('token', response.data)
            this.setIsAuth(true)
        } catch (e) {
            console.error(e)
        } finally {
            this.setIsLoading(false)
        }
    }

}