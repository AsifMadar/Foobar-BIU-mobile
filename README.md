# Foobar-BIU-mobile

## Wiki
This readme file covers the technical aspects of the project. For extensive documentation of all features, including screenshots, refer to [the wiki](https://github.com/michaelts1/Foobar-BIU-mobile/wiki).

## Part 4
This is the branch for **part 4** of the project.
* The code for **part 3** can be found on the branch [part3](https://github.com/michaelts1/Foobar-BIU-mobile/tree/part3).
* The code for **part 2** can be found on the branch [part2](https://github.com/michaelts1/Foobar-BIU-mobile/tree/part2).

## Links to all repositories
* https://github.com/AsifMadar/Foobar-BIU - The repository for the bloom filter
* https://github.com/michaelts1/Foobar-BIU-backend - The repository for the Social App backend server
* https://github.com/michaelts1/Foobar-BIU-mobile - The repository for the Social App Android application
* https://github.com/michaelts1/Foobar-BIU-web - The repository for the Social App web application

## Running this project
This project is dependent on the backend server, so before running this project, you will need to set up and run [Foobar-BIU-backend](https://github.com/michaelts1/Foobar-BIU-backend).

To run the project, open it in Android Studio and click the "Run" button at the top (ctrl+f5).

When the app first loads, you will be taken to the login page. Since you don't have an account yet, click the "Sign Up Page" button to go to the sign up page. After filling the fields in the sign up page (the exact requirement for each field is listed next to the field), click the "Sign Up" button. You wil be taken to the feed screen, where you can scroll over the existing posts and create new posts. Additionaly, each post has a comment button that takes you to the comments list for that post, where you can write comments, like other comments (by clicking "Like") and edit/delete comments.

In the menu, which you can open by clicking the vertical ellipsis on the bottom-right, you can log out of your account (which will take you to the login screen), and turn dark-mode on or off.

### Notes
1. This code assumes your server is hosted at `10.0.2.2:8080`. If your server is hosted on a different url, edit `BaseUrl` in `app/src/main/res/values/strings.xml` accordingly.
2. The creation time of a post is displayed according to your local timezone. If you see an incorrect timestamp, make sure your phone/emulator is set to UTC+3 (or to UTC+2 if you run this project before DST).

## Workflow
At the start of the project, we sat down and planned the application structure, splitted the work between the three of us (Asif and Hodaya, and Michael) and created a sprint with the appropriate tasks on Jira. Afterwords we started working on the project, starting with the web application, and later moving on to the android application.

Originally, we splitted the work between the three of us pretty evenly. Unfortunately on the second week Michael fell very ill, which meant some of the work he was supposed to do had to be done by others. Towards the end of the project, after Michael got better, he also worked on the mobile application, altough less than Asif and Hodaya did.

Our workflow was: selecting a task assigned to me on Jira, working on it in my own branch, and then selecting another related task and working on it as well. After completing the work on a part of the application (for example having both the sign-in and sign-up pages ready), opened a pull request for review by the 2 other members. After everyone approves the pull request, one of us merges it into main.
