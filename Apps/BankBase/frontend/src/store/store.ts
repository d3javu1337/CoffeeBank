import {makeAutoObservable} from "mobx";
import {ClientCompact} from "../dto/model/client/ClientCompactDto";
import AuthService from "../service/AuthService";
import axios, {HttpStatusCode} from "axios";
import {api_url} from "../http";

export default class Store {


    isAuth = localStorage.getItem('token') !== null
    isLoading = false

    constructor() { makeAutoObservable(this) }

    setIsAuth(isAuth: boolean) {
        this.isAuth = localStorage.getItem('token') !== null;
    }

    setIsLoading(isLoading: boolean) {
        this.isLoading = isLoading
    }

    async login(email: string, password: string) {
        this.setIsLoading(true)
        try {
            const response = await AuthService.login(email, password);
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
            localStorage.setItem('token', response.data)
            this.setIsAuth(true)
        } catch (e) {
            console.error(e)
        } finally {
            this.setIsLoading(false)
        }
    }

    async logout() {
        this.setIsLoading(true)
        try {
            localStorage.removeItem('token')
            this.setIsAuth(false)
        } catch (e) {
            console.error(e)
        } finally {
            this.setIsLoading(false)
        }
    }

}