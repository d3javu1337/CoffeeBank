import {CardType} from "../../model/card/CardType";


export default class CardCreateRequest {
    constructor(public readonly type: CardType) {}
}