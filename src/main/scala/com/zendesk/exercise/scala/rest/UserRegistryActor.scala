package com.zendesk.exercise.scala.rest

import akka.actor.{ Actor, ActorLogging, Props }

final case class User(
  id: Int,
  url: Option[String],
  externalId: Option[String],
  name: Option[String],
  alias: Option[String],
  createdAt: Option[String],
  active: Option[Boolean],
  verified: Option[Boolean],
  shared: Option[Boolean],
  locale: Option[String],
  timezone: Option[String],
  lastLoginAt: Option[String],
  email: Option[String],
  phone: Option[String],
  signature: Option[String],
  organizationId: Option[Int],
  tags: Option[List[String]],
  suspended: Option[Boolean],
  role: Option[String]
)

final case class Users(users: Seq[User])

object UserRegistryActor {
  final case class ActionPerformed(description: String)
  final case object GetUsers
  final case class CreateUser(user: User)
  final case class GetUser(id: String)
  final case class DeleteUser(id: String)

  def props: Props = Props[UserRegistryActor]
}

class UserRegistryActor extends Actor with ActorLogging {
  import UserRegistryActor._

  var users = Set.empty[User]

  def receive: Receive = {
    case GetUsers =>
      sender() ! Users(users.toSeq)
    case CreateUser(user) =>
      users += user
      sender() ! ActionPerformed(s"User ${user.name} created.")
    case GetUser(id) =>
      sender() ! users.find(_.id == id)
    case DeleteUser(id) =>
      users.find(_.id == id) foreach { user => users -= user }
      sender() ! ActionPerformed(s"User $id deleted.")
  }
}
