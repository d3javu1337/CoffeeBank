import api from './axios';

export interface ContactPersonCreate {
    surname: string;
    name: string;
    patronymic: string;
    phoneNumber: string;
    email: string;
}

export interface ContactPersonRead {
    id: number;
    surname: string;
    name: string;
    patronymic: string;
    phoneNumber: string;
    email: string;
}

export interface ContactPersonUpdate {
    id: number;
    surname: string;
    name: string;
    patronymic: string;
    phoneNumber: string;
    email: string;
}

class ContactPersonService {
    async getAll(): Promise<ContactPersonRead[]> {
        const response = await api.get<ContactPersonRead[]>('/api/contact-person');
        return response.data;
    }

    async getById(id: number): Promise<ContactPersonRead> {
        const response = await api.get<ContactPersonRead>(`/api/contact-person?id=${id}`);
        return response.data;
    }

    async create(data: ContactPersonCreate): Promise<void> {
        await api.post('/api/contact-person', data);
    }

    async update(data: ContactPersonUpdate): Promise<void> {
        await api.put('/api/contact-person', data);
    }

    async delete(personId: number): Promise<void> {
        await api.delete(`/api/contact-person?personId=${personId}`);
    }
}

export default new ContactPersonService();