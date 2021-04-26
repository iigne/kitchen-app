import axios from "axios";

const noAuthConfig = {"Content-Type": "application/json"};
const authConfig = {}

export default function getAuth() {
    const user = JSON.parse(localStorage.getItem('user'));
    if (user && user.token) {
        return { Authorization: 'Bearer ' + user.token };
    } else {
        return {}
    }
}

export const register = (data, successCallback, errorCallback) => {
    post('auth/register', data, noAuthConfig, successCallback, errorCallback);
}

export const login = (data, successCallback, errorCallback) => {
    post('auth/login', data, noAuthConfig, successCallback, errorCallback);
}

const post = (url, config, data, successCallback, errorCallback) => {
    axios.post(url, config, data)
        .then(res => {
            successCallback(res);
        })
        .catch(error => {
            console.log(error);
            errorCallback(error);
        })
}

const get = () => {

}