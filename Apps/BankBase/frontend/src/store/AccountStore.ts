import {makeAutoObservable} from "mobx";
import AccountCompact from "../dto/model/account/AccountCompactDto";


export default class AccountStore{

    constructor() {makeAutoObservable(this)}

    account = {} as AccountCompact

    setAccount(account: AccountCompact){
        this.account = account;
    }

    get Account(): AccountCompact{
        return this.account;
    }


}