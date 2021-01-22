package dev.einsjannis.db

abstract class Table<TABLE : Table<TABLE>>(val database: Database, val name: String) {

    init {
        database.tables += this
    }

    @Suppress("UNCHECKED_CAST")
    fun insert(row: Row<TABLE>): Unit = this.database.insert(row, this as TABLE)

    @Suppress("UNCHECKED_CAST")
    fun select(columns: List<Column<*, TABLE>>, where: Condition, limit: Int? = null): List<PartialRow<TABLE>> =
        this.database.select(columns, where, this as TABLE, limit)

    @Suppress("UNCHECKED_CAST")
    fun select(where: Condition, limit: Int? = null): List<Row<TABLE>> =
        this.database.select(where, this as TABLE, limit)

    @Suppress("UNCHECKED_CAST")
    fun update(partialRow: PartialRow<TABLE>, where: Condition): Unit =
        this.database.update(partialRow, where, this as TABLE)

    @Suppress("UNCHECKED_CAST")
    fun delete(where: Condition): Unit =
        this.database.delete(where, this as TABLE)

    private val _columns: MutableList<Column<*, TABLE>> = mutableListOf()

    val columns: List<Column<*, TABLE>> get() = _columns.toList()

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> column(serialName: String, databaseType: DatabaseType<T>): Column<T, TABLE> =
        Column(serialName, databaseType, this as TABLE).also { _columns += it }

    protected fun bit(serialName: String) = column(serialName, DatabaseType.Bit)
    protected fun tinyint(serialName: String) = column(serialName, DatabaseType.TinyInt)
    protected fun smallint(serialName: String) = column(serialName, DatabaseType.SmallInt)
    protected fun int(serialName: String) = column(serialName, DatabaseType.Int)
    protected fun bigint(serialName: String) = column(serialName, DatabaseType.BigInt)
    protected fun decimal(serialName: String) = column(serialName, DatabaseType.Decimal)
    protected fun numeric(serialName: String) = column(serialName, DatabaseType.Numeric)
    protected fun real(serialName: String) = column(serialName, DatabaseType.Real)
    protected fun float(serialName: String) = column(serialName, DatabaseType.Float)
    protected fun date(serialName: String) = column(serialName, DatabaseType.Date)
    protected fun time(serialName: String) = column(serialName, DatabaseType.Time)
    protected fun datetime(serialName: String) = column(serialName, DatabaseType.DateTime)
    protected fun timestamp(serialName: String) = column(serialName, DatabaseType.Timestamp)
    protected fun year(serialName: String) = column(serialName, DatabaseType.Year)
    protected fun char(serialName: String, length: Int) = column(serialName, DatabaseType.Char(length))
    protected fun varchar(serialName: String, maxLength: Int) = column(serialName, DatabaseType.VarChar(maxLength))
    protected fun text(serialName: String) = column(serialName, DatabaseType.Text)
    protected fun nchar(serialName: String, length: Int) = column(serialName, DatabaseType.NChar(length))
    protected fun nvarchar(serialName: String, maxLength: Int) = column(serialName, DatabaseType.NVarChar(maxLength))
    protected fun ntext(serialName: String) = column(serialName, DatabaseType.NText)
    protected fun binary(serialName: String, length: Int) = column(serialName, DatabaseType.Binary(length))
    protected fun varbinary(serialName: String, maxLength: Int) = column(serialName, DatabaseType.VarBinary(maxLength))
    protected fun image(serialName: String) = column(serialName, DatabaseType.Image)
    protected fun clob(serialName: String) = column(serialName, DatabaseType.Clob)
    protected fun blob(serialName: String) = column(serialName, DatabaseType.Blob)
    protected fun xml(serialName: String) = column(serialName, DatabaseType.Xml)
    protected fun json(serialName: String) = column(serialName, DatabaseType.Json)
    protected fun <T : Table<T>> row(serialName: String, table: Table<T>) =
        column(serialName, DatabaseType.Row(table))

}
