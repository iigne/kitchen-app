import axios from "axios";
import authHeader from "./auth-header";

class UserIngredientService {
    async getAll() {
        axios.get('/user-ingredient', authHeader())
    }
}