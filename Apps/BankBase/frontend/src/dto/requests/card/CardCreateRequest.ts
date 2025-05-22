import {CardType} from "../../model/card/CardType";


export default class CardCreateRequest {
    constructor(public readonly accountId: number, public readonly type: CardType) {}
}