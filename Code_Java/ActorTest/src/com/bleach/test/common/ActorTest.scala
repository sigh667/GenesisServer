package com.bleach.test.common

import org.scalatest.BeforeAndAfterAll
import org.scalatest.Finders
import org.scalatest.FlatSpecLike
import org.scalatest.Inside
import org.scalatest.Inspectors
import org.scalatest.Matchers
import org.scalatest.OptionValues
import org.scalatest.Suite
import scala.language.postfixOps
import akka.testkit.TestKit
import org.scalatest.MustMatchers
import org.scalatest.mock.MockitoSugar

trait ActorTest extends FlatSpecLike with MustMatchers with OptionValues with Inside with Inspectors with BeforeAndAfterAll with MockitoSugar{
  this: TestKit with Suite =>
  override protected def afterAll() ={
    super.afterAll
    system shutdown
  }
}