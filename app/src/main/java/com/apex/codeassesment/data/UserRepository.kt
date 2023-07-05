package com.apex.codeassesment.data

import com.apex.codeassesment.data.local.LocalDataSource
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.data.remote.RemoteDataSource
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject

// TODO (2 points) : Add tests
// TODO (3 points) : Hide this class through an interface, inject the interface in the clients instead and remove warnings
class UserRepository @Inject constructor(
  private val localDataSource: LocalDataSource,
  private val remoteDataSource: RemoteDataSource
) : UserRepositoryInterface {

  private val savedUser = AtomicReference(User())

  override fun getSavedUser(): User = localDataSource.loadUser()

  override fun getUser(forceUpdate: Boolean): User {
    if (forceUpdate) {
      val user = remoteDataSource.loadUser()
      localDataSource.saveUser(user)
      savedUser.set(user)
    }
    return savedUser.get()
  }

  override fun getUsers(): List<User> = remoteDataSource.loadUsers()
}


interface UserRepositoryInterface {
  fun getSavedUser(): User
  fun getUser(forceUpdate: Boolean): User
  fun getUsers(): List<User>
}
