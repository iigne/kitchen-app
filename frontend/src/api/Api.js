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

export const removeUserIngredients = (data, successCallback, errorCallback) => {
    request("post", '/user-ingredient/remove-quantities', successCallback, errorCallback, authHeader(), data);
}


//Ingredient endpoints
export const createIngredient = (data, successCallback, errorCallback) => {
    request("post", "/ingredient", successCallback, errorCallback, authHeader(), data);
}

export const search = (term, successCallback, errorCallback) => {
    request("get", "/ingredient/search", successCallback, errorCallback, authHeader(),
        {}, {term: term});
}


//Recipe endpoints
export const getRecipes = (type, successCallback, errorCallback) => {
    request("get", "/recipe/list", successCallback, errorCallback, authHeader(),
        {}, {option: type});
}

export const createRecipe = (data, successCallback, errorCallback) => {
    request("post", "/recipe", successCallback, errorCallback, authHeader(), data);
}

export const updateRecipe = (data, successCallback, errorCallback) => {
    request("patch", "/recipe", successCallback, errorCallback, authHeader(), data);
}

export const deleteRecipe = (id, successCallback, errorCallback) => {
    request("delete", '/recipe', successCallback, errorCallback, authHeader(),
        {}, {recipeId: id});
}

export const likeRecipe = (id, successCallback, errorCallback) => {
    request("post", "/recipe/like", successCallback, errorCallback, authHeader(),
        {}, {recipeId: id});
}


//Shopping endpoints
export const getShoppingItems = (successCallback, errorCallback) => {
    request("get", '/shopping', successCallback, errorCallback, authHeader());
}

export const createShoppingItems = (data, successCallback, errorCallback) => {
    request("post", "/shopping/multiple", successCallback, errorCallback, authHeader(), data);
}

export const createShoppingItem = (data, successCallback, errorCallback) => {
    request("post", "/shopping", successCallback, errorCallback, authHeader(), data);
}

export const deleteShoppingItem = (id, successCallback, errorCallback) => {
    request("delete", '/shopping', successCallback, errorCallback, authHeader(),
        {}, {ingredientId: id});
}

export const updateShoppingItem = (data, successCallback, errorCallback) => {
    request("patch", "/shopping", successCallback, errorCallback, authHeader(), data);
}

export const tickShoppingItem = (id, successCallback, errorCallback) => {
    request("post", '/shopping/tick', successCallback, errorCallback, authHeader(),
        {}, {ingredientId: id});
}

export const clearShoppingList = (successCallback, errorCallback) => {
    request("delete", '/shopping/multiple', successCallback, errorCallback, authHeader());
}

export const importShoppingList = (successCallback, errorCallback) => {
    request("post", '/shopping/clear-and-import', successCallback, errorCallback, authHeader());
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