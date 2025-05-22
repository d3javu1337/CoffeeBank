export default class ClientCreate {
    constructor(
        public readonly surname: string,
        public readonly name: string,
        public readonly patronymic: string,
        public readonly dateOfBirth: Date,
        public readonly phoneNumber: String,
        public readonly email: String,
        public readonly password: String) {}
}