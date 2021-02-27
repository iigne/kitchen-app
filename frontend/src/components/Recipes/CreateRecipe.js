import React from "react";
import RecipeForm from "./RecipeForm";

class CreateRecipe extends React.Component  {
    constructor(props) {
        super(props);
        this.state = {
            title: "",
            method: "",
            ingredients: []
        }
    }

    render() {
        return(
          <RecipeForm id={null} title="" method="" ingredients={[]} image={null}/>
        );
    }
}

export default CreateRecipe;