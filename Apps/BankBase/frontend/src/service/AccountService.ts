import {AxiosResponse} from "axios";
import AccountCompact from "../dto/model/account/AccountCompactDto";
import api from "../http";
import {AccountIdRequest} from "../dto/requests/account/AccountIdRequest";
import AccountRename from "../dto/requests/account/AccountRenameRequest";


export default class AccountService {
    static async getAccounts(): Promise<AxiosResponse<Array<AccountCompact>>> {
        return api.get<Array<AccountCompact>>('/account')
    }

    static async getAccount(id: number): Promise<AxiosResponse<AccountCompact>> {
        const data = new AccountIdRequest(id)
        return api.get<AccountCompact>('/account', { data : { data }})
    }

    static async createAccount(): Promise<AxiosResponse<void>> {
        return api.post<void>('/account')
    }

    static async renameAccount(newName: string, accountId: number): Promise<void> {
        const data = new AccountRename(newName, accountId)
        return api.patch('/account', data)
    }
}