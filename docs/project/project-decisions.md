# Project decisions

This document keeps track of important decisions about this project

## List of decisions

- Use Kotlin & Spring Boot instead of Go & the PocketBase framework
- Use Flyway as a database migration tool
- Use Next.js 12 instead of 13 for now
- Use Tailwind for styling
- Work with 2-week sprints
- Write controllers and accept some boilerplate code instead of using Spring Data
    * More flexibility by doing that
    * Learn one thing at a time. Too much magic for us now.
- Use Spring Security and JWT for Authentication/Authorization
- Don't cascade delete data.
    * Keep it and set the foreign key to null
- Work with Ids
    * Don't pass whole objects when they are not needed
- A story should be presentable after it's done
    * Use sub-tasks for the different technical tasks
