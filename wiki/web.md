When first opening the app, we are greeted with the login screen.

The login screen consists of username and password fields, a login button and a "sign up page" button, which will take us to the sign up page.

![](./images/web/1signin.png)

Right now, we still don't have an account. If we try to login anyway with incorrect credentials, we will receive a message saying that the credentials are incorrect.

![](./images/web/2signinInvalidMsg.png)

Going to the sign-up page, we can see 5 different input fields: A username field, password and password-validation fields, a profile picture field and a display name field. Next to each field, the requirements for that field are explicitly stated, to help onboarding the user, and avoiding the frustration of thinking of a great username only to find out that it is invalid.

Under the input fields, we can see a sign up button (more on it in a moment) and a "sign in page" button, which will take us to the sign in page.

![](./images/web/3signup.png)

In addition to the requirements for each field being explicitly stated, the user receives real-time feedback: If a value is valid, the field will become green and will be marked with a green check mark. If the value of the field is invalid, it will become red and will be marked with an exclamation mark, alongside a notice explaining the current value is invalid.

![](./images/web/4signupFilledInvalid.png)

When trying to sign up with invalid details, the sign-up will be rejected and an error message will be displayed to the user.

![](./images/web/5signupFilledInvalidMsg.png)

If all the fields are filled correctly, the user can click "Sign Up", which will create the new account and will automatically login into the newly created account.

![](./images/web/6signupFilledValid.png)

After signing up or logging in, the user is taken to their feed. The feed consists of the last 20 posts from friends of the current user (including the user himself), and the 5 last posts from other users, who are not friends with the current user.

![](./images/web/7homescreen.png)

The top and side navigate bars contain multiple features, with some redundancy to ensure a seamless and intuitive user experience.

Number 1 is the link to the profile page of the current user, which we will explore in depth later. Number 2 is the link to the friends list, which will also be explored soon. Number 3 is the logout button, which can be found in the top menu and also directly on the left sidebar. Number 4 allows toggling dark theme on and off, and number 5 is the link to the feed, which is currently active.

![](./images/web/7homescreenExplained.png)

This is how the feed is displayed when dark theme is enabled. All screens in this application adapt to dark theme, to ensure a seamless integration for dark theme lovers:

![](./images/web/7homescreenDark.png)

The top part of the feed is the post creation box, which is used to create and publish new posts. In this screenshot, we can see the user entered some text in the text box for the new post. The "Post" button, which was disabled while the text box was empty, is now enabled.

![](./images/web/8homescreenNewPostText.png)

The "Add an image..." button can be used to add an image to the post. After adding an image to the post, a preview is displayed and the image can be deleted from the post by clicking the "X" button on the corner of the image.

![](./images/web/9newPostTextPicture.png)

When clicking "Post", the new post is published to the server, and the feed is updated.

![](./images/web/10newPostPosted.png)

However, if the user tries to post a malicious link that has been blacklisted by the administrator, such as in this case:

![](./images/web/11badLink.png)

... then the post is *not* published, and the user is notified of the reason.

![](./images/web/12badLinkMsg.png)

When hovering over a post created by the current user, two buttons appear: "Delete", which will permanently delete the post, and "edit", which will enter editing mode for the post.

![](./images/web/13editingButtons.png)

When in editing mode for the post, the user can freely edit the post's text and add or remove images from the post. When done, clicking "Post" will update the post. Again, trying to insert a blacklisted link to the edited post will be caught by the server, and the above message will be displayed.

![](./images/web/13editingPost.png)


