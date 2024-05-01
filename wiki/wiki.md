Foobar android application wiki
Note: All the actions performed using the server occurs on another thread so it will not ruin the user experience.

First of all, when you open the application the login page will apear
![alt text](./images/android/1.png)

Because we don't got an account we'll create a new one pressing Sign Up Page button

![alt text](./images/android/signup.png)

As you can see, the sign up page has validations and prompts the user when he enters an invalid field

![alt text](<./images/android/sign up fields.png>)

Now, after we set up all fields we will press the Sign Up button which communicate with the server and opens a new user,
then it will send a sign in request to the server and will go to the feed.

![alt text](<./images/android/logout button.png>)

Now we will logout and go directly to the sign in page using the Logout button, clearing the jwt

![alt text](<./images/android/login again.png>)

We will login with invalid details to show you the toast it will do

![alt text](./images/android/invalidsignin.png)

Using the right details we will log in now, the application sending a request to the server and gets back the jwt that stored inside the userDetails singleton and will be reused to do actions like adding posts etc..

![alt text](<./images/android/second feed.png>)

![alt text](<./images/android/adding first post.png>)

After pressing the What's on your mind, there's a UI opened by new activity, I added some content and I will press Post.
When Post pressed it will make a validation so its not a clear post and then will send to the server a request with the details
of the new post. When 200 will be recieved it'll add the post to the feed

![alt text](<./images/android/first post in feed.png>)

![alt text](./images/android/blacklistedpost-1.png)

As you can see, there's a nasty url which we don't want the users will share, luckily our server uses the service of the bloom filter server
which forbid this nasty url, let's see what will happen

![alt text](./images/android/blacklistedtoast.png)

A toast has risen, if you ask me, for this nasty post the user should be banned for life, but the bosses want money.

![alt text](<./images/android/edit screen.png>)

Now, to the edit post feature, I preesed the 3 dots and then edit

![alt text](<./images/android/edit screen-1.png>)

As you can see the full post details appears lets change it

![alt text](<./images/android/edited photo.png>)

![alt text](./images/android/editblacklistpost-1.png)

Trying to add the forbidden url again, using the edit post option

![alt text](./images/android/toastblacklisteditpost.png)

![alt text](./images/android/deleteoption.png)

Lets delete the post

![alt text](./images/android/deletedpostfeed.png)

![alt text](./images/android/likedPost.png)

Added like to the post

![alt text](./images/android/profilepage.png)

After pressing the profile in the bottom bar We got in the user profile page

Now, I will logout and create a new user named Shasiel Prime so he can send a friend request to Mighty Shasiel

![alt text](./images/android/user2feedpage.png)

![alt text](./images/android/MightyFeed.png)

After pressing the MightyShasiel display name we got into his feed, we can't see his info, so lets add him

![alt text](<./images/android/Friend request sent.png>)

![alt text](<./images/android/accepting the request.png>)

Accepting the request after clicking the symbol of friends at the bottom bar, now I will logout and enter Shasiel Prime again
and watch mighty shasiel profile

![alt text](./images/android/MightyShasielprofile.png)

Ta; da 

![alt text](<./images/android/profile like.png>)

Added like

![alt text](./images/android/showinglikeinmightyshasielfeed.png)

Showing the like inside Mighty Shasiel feed

![alt text](<./images/android/edit user option.png>)

Editing MightyShasiel

![alt text](<./images/android/showing the edit fields.png>)

![alt text](<./images/android/showing display name changed in feed.png>)

Edit successfully

![alt text](<./images/android/deleting the user.png>)

deleting the user and going to the sign in page, then showing the feed of Shasiel prime and showing that Mighty Shasiel removed from the DB

![alt text](./images/android/ShasielPrimeFeedAgain.png)

![alt text](./images/android/DB.png)



Server Logs:

![alt text](./images/android/serverlog1.png)

![alt text](./images/android/serverlog2.png)

![alt text](./images/android/serverlog3.png)

BloomFilter:

![alt text](./images/android/image-1.png)

There's no much to show here.

