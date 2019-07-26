Group Project
===
Ethan Horoschak, Jordan Peel, Sarah Adebabay

# Financial Planning for Kids (Temporary)

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
A financial planning app focused on helping youth (middle school to college) create realistic goals for buying products, saving for college, and/or retirement to teach financial literacy at a young age. For example kids can create detailed financial plans for purchasing a car, Xbox, phone, etc. Links with a user's or user's parent's bank account for automatic or manual money transferring into the app to save for a goal. Parent's can monitor their child's account and set up an allowance if wanted. Financial plans update as preferences and details change and goals can be cancelled, returning money as needed. Rewards are given for meeting goals and reaching financial literacy milestones (giftcards, discounts, badges, etc.).


### App Evaluation
[Evaluation of your app across the following attributes]
- **Mobile:**
    - Push notifications for reminders/approval 
    - Contacting parents through text/email
    - Smooth UI with multiple views/pop ups/animations suited for mobile app
    - Use fingerprint or face id for login/ approval for money transfer
- **Story:**
    - Teaching kids financial literacy is a big focus for parents and often neglected in school
    - Youth often change their spending habits/lifestyle around how much money they have and are not realistic about financial goals. If we can create financial goals that reserve funds in the app, they will adapt and be able to complete the goals they set for themselves
    - Parents want to monitor their kids spending and create an easier way to provide allowance to their kids
    - Other apps are very retirement/college focused, but kids often do not care enough about saving so early for those things, so we offer product goals that entice youth to use the app.
- **Market:**
    - Middle School to College kids with bank account
    - Likely kids with recurring income: job, allowance
    - Parents who want to monitor kids spending and distribute allowance
    - Kids who want to gain rewards while saving 
- **Habit:**
    - Parents would likely view the app weekly to check in on kids saving and whenever approval for a transaction is needed
    - Kids would check whenever notification was sent: money moved, goal met, reward gained, approval received, allowance distributed
    - Once a goal is set up, not too much checking would be needed, but would open whenever a new goal is wanted
- **Scope:**
    - We definitely have enough features to fill the time of the program, we will likely be limited by the 5-weeks in terms of completing all possible features. Some features (like bank account linking) may have to be hardcoded in order to complete the app in a reasonable time. Many features are outside of the scope of the code path program, so we will have to do our own research and learning.
    - The Stripped down version of the app will still be interesting to build in terms of captivating UI/UX, real time UI updating and plan processing, bank account linking, storing money on app, parent version. 
    - V1: create account, create financial goals in List view and can set up timeline/plan, access detail view of each goal
    - V2: link bank account, add money transfer features
    - V3: parent version/portal, link account, allowance transfer
    - V4: Rewards program, system for kids without bank account
    - V5: any other extra features

Guiding Questions 

Mobile: How uniquely mobile is the product experience?
- What makes your app more than a glorified website?
- Try for 2 or more of these: maps, camera, location, audio, sensors, push, real-time, etc

Story: How compelling is the story around this app once completed?
- How clear is the value of this app to your audience?
- How well would your friends or peers respond to this product idea?

Market: How large or unique is the market for this app?
- What's the size and scale of your potential user base?
- Does this app provide huge value to a niche group of people?
- Do you have a well-defined audience of people for this app?

Habit: How habit-forming or addictive is this app?
- How frequently would an average user open and use this app?
- Does an average user just consume your app or do they create?

Scope: How well-formed is the scope for this app?
- How technically challenging will it be to complete this app by the end of the program?
- Is a stripped-down version of this app still interesting to build?
- How clearly defined is the product you want to build?

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* New user can sign up for an account
    * If you are younger than 18, you MUST have a parent account linked to your own.
    * If you are 18 and older, a parent account is optional.
* Exisiting user can log in
* User can add a goal
    * User can search for pre-defined goals
    * User can define a custom goal
* User can view a list of all of their current goals
* User can edit the details of a new or existing goal
* User can view their goal/plan details
    * User can edit their goal/plan
    * User can cancel their goal and have money returned
* User can earn rewards for practicing good money habits
* User can view their rewards/reward progress
* User deposit money towards a goal
    * User can set up an automatic payment
    * User can manually deposit a custom amount
* Parents can approve child's requests to do things with money

**Optional Nice-to-have Stories**

* User can turn off parent aproval
    * Parents can turn off approval after child is 16
    * Children can turn off approval after they are 18
* Parents can set up an allowance for their children
    * Parents can change the amount or frequency of the allowance
    * Parents can freeze or delete the allowance
* Parents can use biometic authentication for approval
* User can refer friends to the app
    * User can ear additional rewards for referrals
* User can receive a push notification with a Daily Money Tip
* Animations
* user can upload url and autofill goal info
* can live update item price/sale 
* collaborative goals - multiple users same goal
* option to tell you how much you can spend instead of save
* record expense - can take picture to autofill from receipt picture - recalculate expenses
* graph progress of saving
* look at kids messenger for example of parent approval 
* daily/monthy saving goal
### 2. Screen Archetypes

* Log in/Sign up
   * New user can sign up for an account
   * Exisiting user can log in
* Goals List
   * User can view a list of all of their current goals
   * User can add a goal
* New Goal
    * User can search for pre-defined goals
    * User can define a custom goal
* Edit Goal
    * User can edit the details of a new or existing goal
* Goal Details
    * User can view their goal/plan details
    * User can edit their goal/plan
    * User can deposit money towards a goal
* Deposit
    * User can set up a fixed automatic payment
    * User can manually deposit a custom amount
* Rewards
    * User can earn rewards for practicing good money habits
    * User can view their rewards/reward progress
* Approve Request
    * Parents can approve child's requests to do things with money
    * Parents can use biometic authentication for approval
* Allowance Management
    * Parents can change the amount or frequency of the allowance
    * Parents can freeze or delete the allowance
* Refer a Friend
    * User can refer friends to the app
* Parent Main Screen
    * Parents can set up an allowance for their children
    * Parents can turn off approval after child is 16



### 3. Navigation

**Tab Navigation** (Tab to Screen)

Child View
* Account Tab
* Goals Tab
* Rewards Tab

Parent View
* Account Tab
* Children Tab

**Flow Navigation** (Screen to Screen)

**Child View**
* Log in Screen
   * Account Creation if no account
       * profile set up 
           * home page/goals page
    * home page/goals page
* Goals Page
   * Individual goal details
       * Goals edit page
       * Money transfer page
* Account Page
    * Settings
    * Bank Information/Linking
        * bank link API
    * Parent Set Up/Controls
    * Refer a Friend
* Rewards Page (List of rewards)
    * Individual reward details page
    * Possible reward page

Parent View
* Log in Screen
    * Account Creation if no account
       * profile set up/link with childs
           * Home page
    * Home Page
* Account Page
    * Settings
    * Bank Information/Linking
        * bank link API
    * Parent Set Up/Controls
* Children Tab
    * List of Children linked
        * individual child details view
            * child controls/settigs view

## Wireframes
[Add picture of your hand sketched wireframes in this section]

![](https://i.imgur.com/yFHTyvl.jpg)

![](https://i.imgur.com/B6HDBMZ.jpg)

![](https://i.imgur.com/p0oqxVr.jpg)

![](https://i.imgur.com/XWgb74M.jpg)

![](https://i.imgur.com/gpmPxOq.jpg)

<img src="YOUR_WIREFRAME_IMAGE_URL" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 

 
### Models
Model: User
 Property | Type | Description |
 ------| ----| -----|
 name | String | name of the user |
 email| String | email address of user / login |
 birthday | Date | to determine age of user for certain features |
 password | String | password of account
 BankAccounts | Array<BankAccount>| List of linked bank accounts |
 isParent | Boolean | whether a user is a parent or not 
 parents | Array<User> | list of user's parent users
 children | Array<User> | list of children of user |
 goals | Array<Goal> | list of goals made
 inviter | Pointer: User | User who invited this user
 allowance | Number: double | amount parents transfer to user
 allowanceFrequency| String | how frequent allowance is given
 badges | Array<Reward> | list of badges/rewards user has completed |
 requireAprroval | Boolean | Whether user needs parent approval for transactions |
 estimatedIncome | Number: double | how much user estimates a month |
 
 Model: Bank Account
 Property | Type | Description |
 ------| ----| -----|
 accountNumber | Long/String* | Bank account number |
 routingNumber | Long/String* | Routing number of bank |
 BankName | String | Official name of Bank |
 legalName | String | Legal name on Bank account
 
 Model: Goal
 Property | Type | Description |
 ------| ----| -----|
 name | String | Title of Goal/object |
 endDate| Date | set end date of goal |
 amountSaved | Number: Double | Amount saved for this goal |
 totalCost | Number: Double | total cost of goal/item |
 completed | Boolean | Whether goal has been completed or not |
 image | File | image of item/goal | 
 autoWithdraw | Boolean | Whether autowithdraw is enabled |
 autoWithdrawAmount | Number: Double | amount to auto withdraw |
 shared | Boolean | whether is shared with other users |
 SharedUsers | Array<User> | list of users who share this goal |
 
 Model: Reward
 Property | Type | Description |
 ------| ----| -----|
 name | String | title of goal |
 dateCompleted | Date | when goal was completed |
 completed | Boolean | Whether goal is completed |
 discount | String | Discount code
 discountCompany | String | Where discount is valid
 
### Networking
- Log in screen
    - (Read/GET) User authentication for log in
- Sign up screen 
    - (Create/Post) new user to server and authenticate
- Home Goals List
    - (Read/GET) List of goals and display
- Add Goal activity
    - (Create/POST) new goal to server
- edit goal
    - (Update/PUT) Update information about goals
    - (Remove/DELETE) remove a goal from the server
- Goal Details
    - (Read/GET) retrieve more details about individual goal and display
- Badges View
    - (Read/GET) display list of rewards
- Profile view
    - (Read/GET) user information and display
- Edit profile
    - (Update/PUT) update user information 
- Parent Home view
    - (Read/GET) List of child users and display
- Edit Child/Details View
    - (Read/GET) more details about individual child
    - (Update/PUT) change child settings
    
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
