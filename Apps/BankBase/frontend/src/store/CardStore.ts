import {makeAutoObservable} from "mobx";
import {CardCompact} from "../dto/model/card/CardCompactDto";


export default class CardStore {

    constructor() {makeAutoObservable(this)}

    cards = {} as CardCompact[]

    setCards(cards: CardCompact[]) {
        this.cards = cards;
    }

    get Cards(): CardCompact[] {
        return this.cards;
    }

}