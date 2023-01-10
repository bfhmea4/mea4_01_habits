# Frontend

Our habit tracking web app is built with Next.js, a popular framework for building server-rendered React applications, and uses the utility-first CSS framework tailwind CSS to create fast-loading, responsive, and customizable designs. The frontend of our app is written in TypeScript, a typed superset of JavaScript, and we use Yarn as our package manager to ensure that our app has all the necessary components and resources it needs to run smoothly and efficiently.

At the core of our app is a custom-built habit tracking system that allows users to log and track their daily activities and habits. This system is built using a combination of React components and Axios for managing application state. Users can create and manage their habits, and log their activities for each habit on a daily basis. The app also provides a variety of filters and views to help users track their progress and stay motivated.

Additionally, the app includes a journaling feature that allows users to reflect on their progress and goals, and to share their thoughts and experiences with other users. This feature is built using a combination of React components and a custom-built backend API for managing user data and interactions.

Overall, our habit tracking app is designed to be fast, responsive, and easy to use, providing users with the tools they need to build and maintain healthy habits.

## Structure

The frontend architecture of our Next.js app is organized into a hierarchical structure of components. At the top level, we have a root component that acts as the entry point for the app and manages the overall layout and navigation.

Within the root component, we have several smaller components that are responsible for specific tasks and features. For example, we have a header component that manages the app's header, a footer component that manages the app's footer, and a main content area component that manages the app's main content area.

Each of these top-level components is made up of smaller, more focused components. For example, the header component might contain components for the app's logo, the navigation menu, and the user's profile. The main content area component might contain components for displaying the user's habits, logging daily activities, and viewing progress reports.

This hierarchical structure allows us to easily reuse and modularize our code, making it easier to maintain and update the app over time. It also allows us to easily add new features and functionality by adding new components to the existing structure.

### Assets

The `public` folder is where we store all of the assets that we want to make available to the client-side application. This typically includes things like images, logos, and other static files that are used by the app.

The public folder is special because the contents of this folder are automatically served by the Next.js server without the need for any additional configuration. This means that any files placed in the public folder can be accessed by the app simply by referencing the file's path relative to the public folder.

For example, if we have an image called logo.png in the public folder, we can reference it in our app by using the URL `/logo.png`. This will automatically serve the file from the public folder, allowing us to easily include it in our app.

Overall, the public folder is a convenient way to manage and serve the assets that our app needs in order to function properly. It allows us to keep all of these assets organized in a single location and access them easily from anywhere in the app.

### Lib and Utils

The `lib` folder is where we can store any helper functions or utility code that is used by the app. This can include things like functions for parsing dates, formatting numbers, or performing other common tasks that are used throughout the app.

The lib folder is a convenient place to store this type of code because it is automatically loaded by Next.js when the app starts. This means that we can easily import and use these helper functions from anywhere in the app without having to worry about manually importing them or dealing with complicated file paths.

For example, if we have a helper function for parsing dates in the lib folder, we can use it in our app by importing it from the lib folder and calling it like any other function. This makes it easy to reuse and share common code between different parts of the app, reducing the need for duplicate code and making the app more maintainable.

Overall, the lib folder is a useful tool for organizing and sharing helper functions and utility code in a Next.js app. It allows us to keep this type of code organized and easily accessible from anywhere in the app.

### Context

The `context` folder is where we can store React context objects that are used by the app. This can include things like an authentication context for managing user authentication, a localization context for managing internationalization, or other context objects that are used throughout the app.

The context folder is a convenient place to store these context objects because it allows us to easily import and use them from anywhere in the app. This makes it easy to share data and state between different components in the app, without the need for prop drilling or other complex patterns.

For example, if we have an authentication context in the context folder, we can use it in our app by importing it from the context folder and using the `useContext` hook to access the context data and functions in any component. This makes it easy to manage user authentication and access control throughout the app, without having to pass data and functions through multiple levels of components. We wrap the app in the context provider in the `_app.tsx` file, and then we can access the context data and functions from anywhere in the app.

Overall, the context folder is a useful tool for organizing and sharing React context objects in a Next.js app. It allows us to keep these objects organized and easily accessible from anywhere in the app.

### Pages

In a Next.js app, the `pages` directory is where we define the routes and components for our application. Each file in the pages directory represents a different route in the app, and the contents of the file define the component that will be rendered when that route is accessed.

For example, if we have a file called login.tsx in the pages directory, this will define a route for the URL `/login`, and the contents of the file will define the component that will be rendered when this route is accessed.

Next.js automatically generates the routing for our app based on the files in the pages directory, so we don't have to worry about configuring routing manually. This makes it easy to add new routes and components to our app, and to organize our app into logical, modular units.

Overall, the pages directory is a central part of the Next.js framework, and is where we define the routes and components for our app. It allows us to easily add new routes and components to our app, and to organize our app into logical, modular units.
