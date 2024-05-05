# Backend Overview

## Running the Backend Server
The backend server is written using NodeJS, and depends on the [Bloom Filter Server](./bloomFilter.md) for blacklist functionality.

**Important:** The backend server requires Node.js 20.10.0 or higher. The reasons for that are explained below.

To run the backend server, we perform the following steps:

### Step 0: Running the Backend Server
To run the backend server, we first need to follow the instructions for running the bloom filter server, which you can find in the wiki page for the bloom filter server (linked above).

### Step 1: Setting up the MongoDB Database.
The backend server is using MongoDB to store data and offer a persistent experience to users. So in order to be able to run the backend server, we first need to setup MongoDB on the system. This guide will assume you are running on Windows.

1. Install MongoDB Community Server: https://www.mongodb.com/try/download/community
2. If it wasn't installed automatically, install MongoDB Compass: https://www.mongodb.com/products/tools/compass
3. Create a folder named "C:\data\db".
4. Run the MongoDB server with `&"C:\Program Files\MongoDB\Server\7.0\bin\mongod.exe"` (assuming you installed version 7.0).

### Step 2: Setting up the NodeJS environment
The backend server utilizes an ENV file to store global variables such as the database URL or the secret used to sign user credentials. ENV files are only supported starting with Node.js 20.6.0. Aditionally, the backend server utilizes [import attributes](https://nodejs.org/api/esm.html#import-attributes) to seed the empty database with some initial sample data. Import attributes are only supported starting with Node.js 20.10.0. To summarize, you will need Node.js 20.10.0 (released on 22/11/2023) or higher to run this project, unless you are willing to manually adjust the code (which shouldn't be hard; simply drop the initial seeding of data, and manually set the environment variables instead of using the ENV files).

To set up the NodeJS environment, we will follow these steps:
1. Make sure your Node.js installation is at least version 20.10.0. Otherwise, loading environment variables from `.env.local` won't work, meaning you will have to set these variables from your terminal, **and** you will have to remove line 10 (`initializeDB()`) from `src/app.js`, meaning you will start with an empty database.
2. In the repository root folder, copy `config/.env` into a file named `config/.env.local`. This file will contain the environment variables required to run the backend server.
3. If you want, change the variables in `.env.local`.
	* *You probably want to change at least `JWT_KEY` to something random.*
	* Note that if you changed the port for the bloom filter server, you will have to update `BLOOM_FILTER_PORT` accordingly.
	* Likewise, You will have to set `DB_NAME` and `CONNECTION_STRING` according to how you set up the MongoDB database.
4. You can edit `config/blacklist.txt`, if you want. Each line will be inserted into the bloom filter, and posts containing a link that matches one of the entries in that list will be rejected by the server.
5. Install dependencies, using `npm install`.

### Step 3: Running the server
Finally, all that is left to do is to run the server using `npm start`.

## Accessing and Using the Web Application
