import {AxiosResponse} from "axios";
import {ClientCompact} from "../dto/model/client/ClientCompactDto";
import api from "../http";


export default class ClientService {
    static async getClientInfo(): Promise<AxiosResponse<ClientCompact>> {
        return api.get<ClientCompact>('/client')
    }
}