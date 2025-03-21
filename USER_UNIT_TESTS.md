# cp1

## UserTest Documentation

| Test Method                     | Description                                                                                             | Assertions                                                                                                                                                                                                                                                                                                         |
| ------------------------------- | ------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| `testUserConstructorAndGetters` | Verifies the constructor that takes username and password, and the getters for basic user attributes. | `assertEquals` to check username, password, `assertTrue` to check isActive (should be true), `assertFalse` to check isAdmin (should be false), and `assertNull` to verify`testUserAllArgsConstructor`       | Tests the constructor that initializes all User fields.                     | `assertEquals` verifying all User fields that they contain the provided value during the constructor's execution. |
| `testUserSetters`               | Tests the setter methods for all User fields.                                                            | `assertEquals` verifying the updated values set by the setters for id, username, password, isActive, isAdmin, createdAt, and updatedAt.                                                                                                                                                                             |
| `testDeactivate`                | Tests the `deactivate()` method to ensure it sets the `isActive` flag to false. | `assertFalse` to check if `isActive` is set to false after calling `deactivate()`.                                                                                                                                                                                                                                             |


![User Test](https://drive.google.com/uc?id=1kCnZkl5Vo6m2ZiYFkiSm1qAX6DGfurk4)

---

## UserServiceTest Documentation

| Test Method          | Description                                                                                                 | Mocked Interactions                                                           | Assertions                                                                                                                                                                    |
| -------------------- | ----------------------------------------------------------------------------------------------------------- | ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `testAllUsers`       | Tests the `allUsers()` method to verify that it returns a list of all users from the `UserRepository`.      | `when(userRepository.findAll()).thenReturn(users)`                            | `assertEquals` to check the size of the returned list and the username of a user in the list, `verify` to ensure `userRepository.findAll()` was called exactly once.          |
| `testFindByUsername` | Tests the `findByUsername()` method to verify that it returns the correct user when given a valid username. | `when(userRepository.findByUsername(username)).thenReturn(Optional.of(user))` | `assertTrue` to check if an Optional is present, `assertEquals` to verify the username, `verify` to ensure `userRepository.findByUsername(username)` was called exactly once. |
| `testGetUserById`    | Tests the `getUserById()` method to verify that it returns the correct user when given a valid user ID.     | `when(userRepository.findById(id)).thenReturn(Optional.of(user))`             | `assertTrue` to check if an Optional is present, `assertEquals` to verify the user ID, `verify` to ensure `userRepository.findById(id)` was called exactly once.              |

![User Service](https://drive.google.com/uc?id=1PIw3SPlh23DjSLFUW2WQi2lB1knrTiNe)

---

## UserControllerTest Documentation

| Test Method            | Description                                                                                                             | Mocked Interactions                                                                                                                                                                                 | Assertions                                                                                 |
| ---------------------- | ----------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------ |
| `testGetCurrentUser`   | Tests the `getCurrentUser()` method to verify that it returns the correct `UserResponseDTO` for the authenticated user. | `when(userDetails.getUsername()).thenReturn(username)`, `when(userService.findByUsername(username)).thenReturn(Optional.of(user))`                                                                  | `assertEquals` to verify the HTTP status code (OK) and the user ID in the response body.   |
| `testAllUsersAdmin`    | Tests the `allUsers()` method for an admin user to verify that it returns all users from the service.                   | `when(userDetails.getUsername()).thenReturn(adminUsername)`, `when(userService.findByUsername(adminUsername)).thenReturn(Optional.of(adminUser))`, `when(userService.allUsers()).thenReturn(users)` | `assertEquals` to verify the HTTP status code (OK) and the size of the returned user list. |
| `testAllUsersNotAdmin` | Tests the `allUsers()` method for a non-admin user to verify that it returns a FORBIDDEN HTTP status code.              | `when(userDetails.getUsername()).thenReturn(username)`, `when(userService.findByUsername(username)).thenReturn(Optional.of(nonAdminUser))`                                                          | `assertEquals` to verify the HTTP status code (FORBIDDEN).                                 |

![User Controller](https://drive.google.com/uc?id=1ZJ0XC4QCe41cn16Smt2atl6JFCokZ8-k)
