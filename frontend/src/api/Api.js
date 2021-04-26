import axios from "axios";
import authHeader from "./auth-header";

//Auth endpoints
export const register = (data, successCallback, errorCallback) => {
    request("post", 'auth/register', successCallback, errorCallback, {}, data);
}

export const login = (data, successCallback, errorCallback) => {
    request("post", 'auth/login', successCallback, errorCallback, {}, data);
}

//User ingredient endpoints
export const getUserIngredients = (successCallback, errorCallback) => {
    request("get", '/user-ingredient', successCallback, errorCallback, authHeader());
}

export const createUserIngredient = (data, successCallback, errorCallback) => {
    request("post", '/user-ingredient', successCallback, errorCallback, authHeader(), data);
}

export const deleteUserIngredient = (id, successCallback, errorCallback) => {
    request("delete", '/user-ingredient', successCallback, errorCallback, authHeader(),
        {}, {ingredientId: id});
}

export const updateUserIngredient = (data, successCallback, errorCallback) => {
    request("patch", '/user-ingredient', successCallback, errorCallback, authHeader(), data);
}

//Ingredient endpoints
export const createIngredient = (data, successCallback, errorCallback) => {
    request("post", "/ingredient", successCallback, errorCallback, authHeader(), data);
}

export const search = (term, successCallback, errorCallback) => {
    request("get", "/ingredient/search", successCallback, errorCallback, authHeader(),
        {}, {term: term});
}

const request = (method, url, successCallback, errorCallback, headers, data, params) => {
    axios.request({
            method: method,
            url: url,
            params: params,
            data: data,
            headers: headers
        }
    ).then(res => {
        if (successCallback) {
            successCallback(res);
        }
    }).catch(error => {
        console.log(error);
        if (errorCallback) {
            errorCallback(error);
        }
    });
}