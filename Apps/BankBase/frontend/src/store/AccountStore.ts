import {makeAutoObservable} from "mobx";
import AccountCompact from "../dto/model/account/AccountCompactDto";


export default class AccountStore{

    constructor() {makeAutoObservable(this)}

    accounts = {} as AccountCompact[]

    setAccounts(accounts: AccountCompact[]){
        this.accounts = accounts;
    }

    get Accounts(): AccountCompact[]{
        console.log(this.accounts);
        return this.accounts;
    }


}