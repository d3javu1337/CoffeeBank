import {AccountType} from "./AccountType";

export default class AccountCompact {
    constructor(public readonly id: number,
                public readonly accountName: string,
                public readonly accountDeposit: number,
                public readonly accountType: AccountType) {
    }
}