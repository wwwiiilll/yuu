package net.archwill.yuu

import org.apache.poi.ss.usermodel.{Cell, CellType}
import org.apache.poi.ss.util.CellAddress

final class CellOps(val self: Cell) extends AnyVal {
  def as[A](implicit cr: CellReader[A]): ReadResult[A] = cr.read(self)
  def asOpt[A](implicit cr: CellReader[A]): Option[A] = cr.read(self).toOption
  def asEither[A](implicit cr: CellReader[A]): Either[Seq[(String, Option[CellAddress])], A] = cr.read(self).toEither

  def valueType: CellType = if (self.getCellTypeEnum == CellType.FORMULA) self.getCachedFormulaResultTypeEnum else self.getCellTypeEnum
}

trait ToCellOps {
  implicit def toCellOps(a: Cell): CellOps = new CellOps(a)
}
