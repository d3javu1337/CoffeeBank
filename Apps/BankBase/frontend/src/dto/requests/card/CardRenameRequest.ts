

export default class CardRenameRequest {
    constructor(public readonly accountId: number, public readonly cardId: number, public readonly newName: string) {}
}