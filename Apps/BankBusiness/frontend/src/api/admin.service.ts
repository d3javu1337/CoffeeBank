import api from './axios';
import type { ContactPersonRead } from './contact-person.service';

export interface BusinessClientCompact {
    id: number;
    officialName: string;
    brand: string;
    email: string;
}

export interface BusinessClientCompactWithContacts {
    id: number;
    officialName: string;
    brand: string;
    email: string;
    contacts: ContactPersonRead[];
}

class AdminService {
    async getClients(): Promise<BusinessClientCompact[]> {
        const response = await api.get<BusinessClientCompact[]>('/api/admin');
        return response.data;
    }

    async getClientById(clientId: number): Promise<BusinessClientCompactWithContacts> {
        const response = await api.get<BusinessClientCompactWithContacts>(`/api/admin/client?clientId=${clientId}`);
        return response.data;
    }
}

export default new AdminService();