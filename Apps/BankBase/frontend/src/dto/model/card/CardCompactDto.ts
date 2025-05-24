import {CardType} from "./CardType";

export interface CardCompact {
    id: number;
    name: string,
    type: CardType,
    number: string,
}