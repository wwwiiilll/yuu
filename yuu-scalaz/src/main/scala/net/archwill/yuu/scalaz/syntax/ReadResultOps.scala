package net.archwill.yuu.scalaz.syntax

import scalaz._

import net.archwill.yuu.{ReadError, ReadResult, ReadSuccess}

final class ReadResultOps[A](val self: ReadResult[A]) extends AnyVal {

  def cata[X](error: Seq[String] => X, success: A => X): X = self.fold(error, success)

  def disjunction: Seq[String] \/ A = self.fold(\/.left, \/.right)

  def validation: Validation[Seq[String], A] = self.fold(Validation.failure, Validation.success)

  def validationNel: ValidationNel[String, A] = self match {
    case ReadSuccess(a) => Validation.success(a)
    case ReadError(h :: t) => Validation.failure(NonEmptyList(h, t: _*))
    case _ => Validation.failureNel("Unknown error")
  }

}

trait ToReadResultOps {
  implicit def toReadResultOps[A](a: ReadResult[A]): ReadResultOps[A] = new ReadResultOps(a)
}
