import {AxiosResponse} from "axios";
import api from "../http";
import ClientCreate from "../dto/requests/client/ClientCreateRequest";
import LoginRequest from "../dto/requests/LoginRequest";


export default class AuthService {
    static async login(email: string, password: string): Promise<AxiosResponse<string>> {
        const data = new LoginRequest(email, password);
        return api.post('/auth/login', data)
    }

    static async registration(surname: string, name: string, patronymic: string, dateOfBirth: Date,
                              phoneNumber: String, email: String, password: String): Promise<AxiosResponse<void>> {
        const data = new ClientCreate(surname, name, patronymic, dateOfBirth, phoneNumber, email, password);
        return api.post('/auth/registration', data)
    }
}