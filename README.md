# Rezeptliste2

When the app is started, you are usually in the Ingredients list tab. The basic structure can be found in the Design folder.

## Ingredient list
In the ingredients list, you enter all the ingredients that you find in your household. However, the list of ingredients has been deliberately limited so that it is only possible to add ingredients that are also is present in a recipe.

To the right of the ingredient there is a trash can icon which you can use to remove an ingredient from the list.

At the bottom right there is a button that opens a text field. Below the text field, all the ingredients that are available for selection are listed below the text field. By clicking on an ingredient below the text field, it is automatically added to the list. The text field itself is used to filter the ingredients.

The purpose of the ingredients list is to provide an overview of which ingredients you already have and which ingredients are available.Further benefits will be explained later.

The second part of the application is the recipe list, which can be accessed by clicking on the Recipe list tab.

### Example:
Recipe A has salt as an ingredient. Because the ingredient salt is present in at least one recipe, you can add salt to the list of ingredients.

No recipe has the ingredient pepper. It is not possible to add the ingredient pepper to the list of ingredients.

Text field: "sa" \
All ingredients containing the text "sa" are displayed. This part can be at the beginning, at the end or somewhere in the middle.

## Recipe list
We are here in the overview for all existing recipes. In the overview we have a picture, name, duration and availability for each recipe.

Now the list of ingredients that we discussed at the beginning makes sense. The availability shows whether we have all the ingredients for this recipe.

Accordingly, we have 3 availability levels.

***Green***: We have all the ingredients \
***Orange***: We have at least one ingredient, but not all of them. Also orange because yellow was difficult to see. \
***Red***: We are missing all ingredients

Let's start with the obvious. There is a button at the bottom right to add a recipe.
The recipes are clickable, which opens a detailed view of the recipe.

What is less obvious is that you can also press and hold on the recipes to open a context menu. 
Perhaps a little unusual, but the menu opens below the recipe.
The context menu offers 2 options. Editing (which is the same as clicking on the recipe normally. But I will go into this later) and Remove.

Another thing worth mentioning is that more recipes are arranged next to each other in landscape format than in portrait format. Fortunately, this wasn't too difficult.

Incidentally, adding a new recipe is the same as the detailed view, but you start with an empty recipe. That's why we won't go into this any further, as it works in exactly the same way as the detailed view.

Then we can start directly with the exciting part, which is where all the magic is.

## Detailed view of the recipe
Just like in the overview, we see the picture, name and duration.
Below we see a list of ingredients and next to it the quantity.
Finally, there are instructions on how to prepare this recipe.

Well, the detailed view looks a bit strange. There are 2 buttons at the bottom. One is a Back button and the other a Done button.
The last line in the ingredients list is also empty.

Let me explain. The detailed view is not just a normal detailed view, but you can also make changes directly in this view.

You want to change the picture? Just click on it. I recommend that you do not use images over 1MB. So it's best to use relatively small images. Otherwise the app will unfortunately be unusable.

To edit the list of ingredients, you have to click on it twice. A small blemish, but text boxes have a bad habit of not executing a click event unless they are disabled. I'm sure it could have been fixed by playing around with the focus manager a bit, but I skipped it due to time constraints.

Oh well. So you have to click twice for ingredients. So what?
You can remove an ingredient by simply removing the name of the ingredient. Ingredients without a name are automatically removed from the list.

To add a new ingredient, there is an empty line at the bottom where you can enter your ingredient. Note, unlike the ingredient list, we are not restricted here as to which ingredient we add. Quite the opposite. We can determine exactly here which ingredients there are within the ingredient list tab.

This means, for example, that if all recipes are removed, there will no longer be any ingredients in the ingredients list.

Editing an existing ingredient simply means that the edited ingredient is removed from the recipe and added as a new ingredient. This is relatively similar to simply adding a new ingredient and removing the other one.

And that brings us to the end. I hope my brief introduction has given you a good overview of how the app works.

## Used Tools
- Android Studio
- DB Browser for SQLite (creating the database for initialization)
- DBeaver (If DB Browser could not do something)
- ChatGPT (For problems that were hard to find)
- GitHub Copilot
- Kotlin
- Jetpack Compose
- Jetpack Compose Room (database)
- Android Developers (courses and documentation)
- Chefkoch (recipes, inspiration for building and images)
- Git
- GitHub

## Known Issues
- The gesture to go back has not been implemented. Although it is possible to switch between the different views via NavHost, which makes it possible to go back between the views, I have opted for a simpler variant, which is why it is only possible to change the view using the buttons.
- Another problem is the image limit. Although it is possible to save images and select them from the gallery, I did not take into account that images within a database can only be a limited size. The limit is therefore around 1 MB per image. Ideally it should be less than that. There are several ways to solve this problem.
  - images are saved in a specified folder and only the path is stored in the database
  - error message if image is too large
  - compress the image until it fits
- Ingredients are case sensitive.
- The font size of the recipes is automatically visually reduced when the user switches to them. Although this is intended, it takes too long. A poor solution for adjusting the font size based on the device size.