import {makeAutoObservable} from "mobx";
import {ClientCompact} from "../dto/model/client/ClientCompactDto";


export default class ClientStore {

    constructor() {makeAutoObservable(this)}

    client = {} as ClientCompact

    setClient(client: ClientCompact) {
        this.client = client
    }

    get Client(): ClientCompact {
        return this.client;
    }


}