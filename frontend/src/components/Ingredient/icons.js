import {faAppleAlt, faBox, faBreadSlice, faLeaf, faPepperHot, faSnowflake} from "@fortawesome/free-solid-svg-icons";

export const icons = new Map([
    ['Vegetable', { icon: faLeaf, color: "#66B447"}],
    ["Fruit", {icon: faAppleAlt, color:"#E9692C"}],
    ["Cupboard",{icon: faBox, color:"#d4bc89"}],
    ["Fridge", {icon: faSnowflake, color:"#a0cbf8"}],
    ["Bread", {icon: faBreadSlice, color:"#EEC07B"}],
    ["Freezer", {icon: faSnowflake, color:"#1530b1"}],
    ["Spice", {icon: faPepperHot, color:"#000000"}],
    ["Other", {icon: null}]
]);