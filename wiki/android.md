Foobar android application wiki
Note: All the actions performed using the server occurs on another thread so it will not ruin the user experience.

First of all, when you open the application the login page will appear

![](./images/android/1.png)

Because we don't got an account we'll create a new one pressing Sign Up Page button

![](./images/android/signup.png)

As you can see, the sign up page has validations and prompts the user when he enters an invalid field

![](<./images/android/sign up fields.png>)

Now, after we set up all fields we will press the Sign Up button which communicate with the server and opens a new user,
then it will send a sign in request to the server and will go to the feed.

![](<./images/android/logout button.png>)

Now we will logout and go directly to the sign in page using the Logout button, clearing the jwt

![](<./images/android/login again.png>)

We will login with invalid details to show you the toast it will do

![](./images/android/invalidsignin.png)

Using the right details we will log in now, the application sending a request to the server and gets back the jwt that stored inside the userDetails singleton and will be reused to do actions like adding posts etc..

![](<./images/android/second feed.png>)

![](<./images/android/adding first post.png>)

After pressing the What's on your mind, there's a UI opened by new activity, I added some content and I will press Post.
When Post pressed it will make a validation so its not a clear post and then will send to the server a request with the details
of the new post. When 200 will be recieved it'll add the post to the feed

![](<./images/android/first post in feed.png>)

![](./images/android/blacklistedpost-1.png)

As you can see, there's a nasty url which we don't want the users will share, luckily our server uses the service of the bloom filter server
which forbid this nasty url, let's see what will happen

![](./images/android/blacklistedtoast.png)

A toast has risen, if you ask me, for this nasty post the user should be banned for life, but the bosses want money.

![](<./images/android/edit screen.png>)

Now, to the edit post feature, I preesed the 3 dots and then edit

![](<./images/android/edit screen-1.png>)

As you can see the full post details appears lets change it

![](<./images/android/edited photo.png>)

![](./images/android/editblacklistpost-1.png)

Trying to add the forbidden url again, using the edit post option

![](./images/android/toastblacklisteditpost.png)

![](./images/android/deleteoption.png)

Lets delete the post

![](./images/android/deletedpostfeed.png)

![](./images/android/likedPost.png)

Added like to the post

![](./images/android/profilepage.png)

After pressing the profile in the bottom bar We got in the user profile page

Now, I will logout and create a new user named Shasiel Prime so he can send a friend request to Mighty Shasiel

![](./images/android/user2feedpage.png)

![](./images/android/MightyFeed.png)

After pressing the MightyShasiel display name we got into his feed, we can't see his info, so lets add him

![](<./images/android/Friend request sent.png>)

![](<./images/android/accepting the request.png>)

Accepting the request after clicking the symbol of friends at the bottom bar, now I will logout and enter Shasiel Prime again
and watch mighty shasiel profile

![](./images/android/MightyShasielprofile.png)

Ta; da 

![](<./images/android/profile like.png>)

Added like

![](./images/android/showinglikeinmightyshasielfeed.png)

Showing the like inside Mighty Shasiel feed

![](<./images/android/edit user option.png>)

Editing MightyShasiel

![](<./images/android/showing the edit fields.png>)

![](<./images/android/showing display name changed in feed.png>)

Edit successfully

![](<./images/android/deleting the user.png>)

deleting the user and going to the sign in page, then showing the feed of Shasiel prime and showing that Mighty Shasiel removed from the DB

![](./images/android/ShasielPrimeFeedAgain.png)

![](./images/android/DB.png)



Server Logs:

![](./images/android/serverlog1.png)

![](./images/android/serverlog2.png)

![](./images/android/serverlog3.png)

BloomFilter:

![](./images/android/image-1.png)

There's no much to show here.

