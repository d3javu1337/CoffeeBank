import {AccountType} from "./AccountType";

export default class AccountCompact {
    constructor(public readonly id: number,
                public readonly accountName: string,
                public readonly AccountDeposit: number,
                public readonly AccountType: AccountType) {
    }
}