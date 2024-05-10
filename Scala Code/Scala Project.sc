import java.io.{File, FileOutputStream, PrintWriter}
import java.time.{LocalDateTime, LocalDate}
import java.time.format.DateTimeFormatter
import scala.io.{BufferedSource, Source}

// Define a logging function
def logEvent(logLevel: String, message: String): Unit = {
  val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
  val logMessage = s"$timestamp $logLevel $message"
  val logFile = new File("C:\\Users\\Karam\\Desktop\\Scala Discounts Project\\Logs_engine.log")
  val writer = new PrintWriter(new FileOutputStream(logFile, true))
  writer.println(logMessage)
  writer.close()
}

val source: BufferedSource = Source.fromFile("C:\\Users\\Karam\\Desktop\\Scala Discounts Project\\Raw Data TRX1000.csv")
val lines: List[String] = source.getLines().drop(1).toList
val f: File = new File("C:\\Users\\Karam\\Desktop\\Scala Discounts Project\\Output.csv")
val writer = new PrintWriter(new FileOutputStream(f, false))

case class Transaction(timestamp: LocalDateTime,
                       productName: String,
                       productCategory: String,
                       expiryDate: LocalDate,
                       quantity: Int,
                       unitPrice: Float,
                       channel: String,
                       paymentMethod: String)

// Log event: Parsing timestamps
logEvent("INFO", "Timestamp parsing completed.")

def parseTimestamp(timestamp: String): LocalDateTime = {
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssX")
  LocalDateTime.parse(timestamp, formatter)
}

def parseDate(dateString: String): LocalDate = {
  val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
  LocalDate.parse(dateString, formatter)
}

def toTransaction(line: String): Transaction = {
  val Array(timestamp, productName, expiryDate, quantityStr,
  unitPriceStr, channel, paymentMethod) = line.split(",")
  val parsedTimestamp = parseTimestamp(timestamp)
  val parsedExpiryDate = parseDate(expiryDate)
  val quantity = quantityStr.toInt
  val unitPrice = unitPriceStr.toFloat
  val product_category = productName.split("-")(0).trim
  Transaction(parsedTimestamp, productName , product_category, parsedExpiryDate, quantity, unitPrice, channel, paymentMethod)
}

// Log event: Checking if less than 30 days remaining
logEvent("INFO", "Checking if less than 30 days remaining completed.")

def lessThan30DaysRemaining(from: LocalDateTime, to: LocalDate): Boolean = {
  val currentDate = LocalDate.from(from)
  val remainingDays = java.time.temporal.ChronoUnit.DAYS.between(currentDate, to)
  remainingDays < 30 && remainingDays >= 0
}

// Log event: Checking if special date
logEvent("INFO", "Checking if special date completed.")

def isSpecialDate(date: LocalDateTime): Boolean = {
  date.getMonthValue == 3 && date.getDayOfMonth == 23
}

// Log event: Checking if product is cheese
logEvent("INFO", "Checking if product is cheese completed.")

def isCheese(productName: String): Boolean = {
  productName.toLowerCase.contains("cheese")
}

// Log event: Checking if product is wine
logEvent("INFO", "Checking if product is wine completed.")

def isWine(productName: String): Boolean = {
  productName.toLowerCase.contains("wine")
}

// Log event: Checking if quantity is eligible
logEvent("INFO", "Checking if quantity is eligible completed.")

def isQuantityEligible(quantity: Int): Boolean = {
  quantity > 5
}

// Log event: Checking if payment method is Visa card
logEvent("INFO", "Checking if payment method is Visa card completed.")

def isVisaCard(paymentMethod: String): Boolean = {
  paymentMethod.toLowerCase == "visa"
}

// Log event: Checking if channel is application
logEvent("INFO", "Checking if channel is application completed.")

def isApplication(channel: String): Boolean = {
  channel.toLowerCase == "app"
}

// Log event: Calculating discounts
logEvent("INFO", "Discount calculation completed.")

def calDiscount(transaction: Transaction): Float = {
  val daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(transaction.timestamp.toLocalDate(), transaction.expiryDate)
  val ExpiryDiscount = if (daysRemaining >= 1 && daysRemaining <= 29) 30 - daysRemaining else 0
  val specialDiscount = if (isSpecialDate(transaction.timestamp)) 50 else 0
  val quantityDiscount = if (isQuantityEligible((transaction.quantity))) {
    transaction.quantity match {
      case q if q >= 6 && q <= 9 => 5
      case q if q >= 10 && q <= 14 => 7
      case q if q >= 15 => 10
      case _ => 0
    }
  } else 0
  val typeCheeseDiscount = if (isCheese(transaction.productCategory)) 10 else 0
  val typeWineDiscount = if (isWine(transaction.productCategory)) 5 else 0
  val VisaCardDiscount = if (isVisaCard(transaction.paymentMethod)) 5 else 0
  val ApplicationDiscount = if (isApplication(transaction.channel)) {
    val reminder = transaction.quantity % 5
    if (reminder == 0) transaction.quantity else transaction.quantity + (5 - reminder)
  } else 0
  val temp_discounts = List(ExpiryDiscount, specialDiscount, quantityDiscount, typeCheeseDiscount, typeWineDiscount, VisaCardDiscount, ApplicationDiscount)
  println(temp_discounts)
  val largestTwoDiscounts = temp_discounts.filter(_ > 0).sorted.reverse.take(2)
  if (largestTwoDiscounts.length >= 2) largestTwoDiscounts.sum.toFloat / 2
  else largestTwoDiscounts.sum
}

// Log event: Starting transaction processing
logEvent("INFO", "Transaction processing started.")

def processTransaction(t: Transaction): String = {
  val discount = calDiscount(t)
  val price = (t.quantity * t.unitPrice) - (t.quantity * t.unitPrice * discount / 100)
  s"${t.timestamp},${t.productName},${t.expiryDate},${t.quantity}," +
    s"${t.unitPrice},${t.channel},${t.paymentMethod},${discount} , ${price}"
}

val transactions = lines.map(toTransaction)
val discounts = transactions.map(processTransaction)

// Log event: Transaction processing completed
logEvent("INFO", "Transaction processing completed.")

discounts.foreach(writer.println)

writer.close()
