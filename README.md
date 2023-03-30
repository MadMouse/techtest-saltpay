# Project Notes

## Wrap up
As with all tech tests there has to be a time when you draw a "line in the sand", alas I have reached the end of the time boxed for this project.

I would like to thank you for the opportunity give a quick handover summary.

##### Roadmap
###### - [Roadmap](docs/Roadmap.md)

##### incomplete features
- Favorites
  1. Core tests are not complete
  2. UI components are hidden not deleted

##### Unit tests
- Due to time constraints there are incomplete unit tests, As I was using a sudo  TDD approach, I implement a single larger test.

##### UI testing (Weakest area)
- Tried to use the new UI test framework with compose and fragments, this was an interesting learning curve and have decided not to include these learnings in the review.   

#####  Area's to be improved
###### - [Known Issues](docs/KnownIssues.md)


## Approach
The project will be treated as a hackathon project time boxed to 20 hours, This will be spread over 5 days with multiple milestones.

The key milestone will be at the 15 hour mark, this will determine the final feature roadmap. The last 5 hours will used to tidy up the code and application for the final delivery.

## Caveats

1. Release should be done via a CI/CD (never a local machine)
2. All code linting and testing should be done before PR is created.
3. All code linting and testing should be done via git hook local.

## Questions to the reviewer

1. What is the estimated time expected for a project of this size.
2. Testing: is the system adequately tested?
   This is a double edged sword as testing and code coverage is key in production code, as this is throw away code.
    - I would like to know what is deemed as adequate?
    - What percentage does testing factor into the final assessment?

1. Document your code as if it were going into production.
    - I would like to know what is expected?
        - Code should be self documenting is written correctly.
        - Release Notes?
        - Penetration testing?
        - Security review?
        - Testing sign off?
---

### Objective

SaltPay is branching into the music business and needs a new Android app.

### Brief

In a fictional world, SaltPay is branching into the music business and we need a new a app. This Android app needs to display the top 100
songs based on the iTunes API. This code challenge allows you to choose your own path and lets you flaunt your creative panache and
technical skills along the way.

### Tasks

- Show top 100 albums based on the json feed here: `https://itunes.apple.com/us/rss/topalbums/limit=100/json`
- A clean modern look
- A good user experience
- Allow the top 100 to be searchable
- Surprise us! Add a feature that you think would work well here (for instance, advanced search, integration with other API, a "Favorite"
  functionality)
    - Describe the feature in separate markdown file

### Deliverables

Make sure to include all code in this repository.

We are expecting a native Android application, so include assignment.apk containing your app **in the root of the repository**.
After your app is built, either via Android Studio or by running the command ./gradlew assembleDebug in your project directory, look in <
project-name>/<module-name>/build/outputs/apk/.

### Evaluation Criteria

- **Android** best practices
- Show us your work through your commit history
- Completeness: did you complete the features?
- Correctness: does the functionality act in sensible, thought-out ways?
- Maintainability: is it written in a clean, maintainable way?
- Testing: is the system adequately tested?

### CodeSubmit

Please organize, design, test and document your code as if it were going into production - then push your changes to the master branch.
After you have pushed your code, you may submit the assignment on the assignment page.

All the best and happy coding,

The SaltPay Team