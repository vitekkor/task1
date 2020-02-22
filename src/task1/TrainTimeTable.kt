package task1

/**
 * Класс "расписание поездов".
 *
 * Объект класса хранит расписание поездов для определённой станции отправления.
 * Для каждого поезда хранится конечная станция и список промежуточных.
 * Поддерживаемые методы:
 * добавить новый поезд, удалить поезд,
 * добавить / удалить промежуточную станцию существующему поезду,
 * поиск поездов по времени.
 *
 * В конструктор передаётся название станции отправления для данного расписания.
 */
class TrainTimeTable(private val baseStationName: String) {
    private val listOfTrains = mutableMapOf<String, Train>()
    /**
     * Добавить новый поезд.
     *
     * Если поезд с таким именем уже есть, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @param depart время отправления с baseStationName
     * @param destination конечная станция
     * @return true, если поезд успешно добавлен, false, если такой поезд уже есть
     */
    fun addTrain(train: String, depart: Time, destination: Stop): Boolean {
        return if (listOfTrains[train] == null) {
            listOfTrains[train] = Train(train, Stop(baseStationName, depart), destination)
            true
        } else false
    }


    /**
     * Удалить существующий поезд.
     *
     * Если поезда с таким именем нет, следует вернуть false и ничего не изменять в таблице
     *
     * @param train название поезда
     * @return true, если поезд успешно удалён, false, если такой поезд не существует
     */
    fun removeTrain(train: String): Boolean = listOfTrains.remove(train) != null

    /**
     * Добавить/изменить начальную, промежуточную или конечную остановку поезду.
     *
     * Если у поезда ещё нет остановки с названием stop, добавить её и вернуть true.
     * Если stop.name совпадает с baseStationName, изменить время отправления с этой станции и вернуть false.
     * Если stop совпадает с destination данного поезда, изменить время прибытия на неё и вернуть false.
     * Если stop совпадает с одной из промежуточных остановок, изменить время прибытия на неё и вернуть false.
     *
     * Функция должна сохранять инвариант: время прибытия на любую из промежуточных станций
     * должно находиться в интервале между временем отправления с baseStation и временем прибытия в destination,
     * иначе следует бросить исключение IllegalArgumentException.
     * Также, время прибытия на любую из промежуточных станций не должно совпадать с временем прибытия на другую
     * станцию или с временем отправления с baseStation, иначе бросить то же исключение.
     *
     * @param train название поезда
     * @param stop начальная, промежуточная или конечная станция
     * @return true, если поезду была добавлена новая остановка, false, если было изменено время остановки на старой
     */
    fun addStop(train: String, stop: Stop): Boolean {
        val newStops = listOfTrains.getValue(train).stops.toMutableList()
        when (stop.name) {
            baseStationName -> {
                if (newStops[1].time <= stop.time) throw IllegalArgumentException()
                newStops[0] = stop
                newStops.sortBy { it.time }
                listOfTrains[train] = Train(train, newStops)
                return false
            }
            listOfTrains.getValue(train).stops.last().name -> {
                newStops.removeAt(newStops.lastIndex)
                if (newStops.last().time >= stop.time) throw IllegalArgumentException()
                newStops.add(stop)
                newStops.sortBy { it.time }
                listOfTrains[train] = Train(train, newStops)
                return false
            }
        }
        require(stop.time in listOfTrains.getValue(train).stops[0].time..listOfTrains.getValue(train).stops.last().time)
        val specialStop = listOfTrains.getValue(train).specificStation(stop.name)
        if (specialStop != null) {
            newStops.remove(specialStop)
            newStops.add(stop)
            newStops.sortBy { it.time }
            listOfTrains[train] = Train(train, newStops)
            return false
        }
        for ((name, time) in listOfTrains.getValue(train).stops) {
            if (time == stop.time) {
                if (stop.name != name) throw IllegalArgumentException() else return false
            }
        }
        newStops.add(stop)
        newStops.sortBy { it.time }
        listOfTrains[train] = Train(train, newStops)
        return true
    }

    /**
     * Удалить одну из промежуточных остановок.
     *
     * Если stopName совпадает с именем одной из промежуточных остановок, удалить её и вернуть true.
     * Если у поезда нет такой остановки, или stopName совпадает с начальной или конечной остановкой, вернуть false.
     *
     * @param train название поезда
     * @param stopName название промежуточной остановки
     * @return true, если удаление успешно
     */
    fun removeStop(train: String, stopName: String): Boolean {
        val stop = listOfTrains.getValue(train).specificStation(stopName)
        return if (stop != null
            && listOfTrains.getValue(train).stops[0].name != stopName
            && listOfTrains.getValue(train).stops.last().name != stopName
        ) {
            val newStops = listOfTrains.getValue(train).stops.toMutableList()
            newStops.remove(stop)
            listOfTrains[train] = Train(train, newStops)
            true
        } else false
    }

    /**
     * Вернуть список всех поездов, упорядоченный по времени отправления с baseStationName
     */
    fun trains(): List<Train> =
        listOfTrains.values.toMutableList().sortedBy { it.specificStation(baseStationName)!!.time }

    /**
     * Вернуть список всех поездов, отправляющихся не ранее currentTime
     * и имеющих остановку (начальную, промежуточную или конечную) на станции destinationName.
     * Список должен быть упорядочен по времени прибытия на станцию destinationName
     */
    fun trains(currentTime: Time, destinationName: String): List<Train> {
        val result = mutableListOf<Train>()
        for ((_, train) in listOfTrains) {
            val condition1 = train.stops.elementAt(0).time >= currentTime
            val condition2 = train.specificStation(destinationName) != null
            if (condition1 && condition2) result.add(train)
        }
        return result.sortedBy { it.specificStation(destinationName)!!.time }
    }

    fun getTrain(trainName: String): Train = listOfTrains.getValue(trainName)

    /**
     * Сравнение на равенство.
     * Расписания считаются одинаковыми, если содержат одинаковый набор поездов,
     * и поезда с тем же именем останавливаются на одинаковых станциях в одинаковое время.
     */
    override fun equals(other: Any?): Boolean = other is TrainTimeTable && this.trains() == other.trains()

    override fun hashCode(): Int {
        var result = 1
        for ((name, train) in listOfTrains) {
            result += name.hashCode() + train.stops.sumBy { it.hashCode() }
        }
        return result
    }
}

/**
 * Время (часы, минуты)
 */
data class Time(val hour: Int, val minute: Int) : Comparable<Time> {
    /**
     * Сравнение времён на больше/меньше (согласно контракту compareTo)
     */
    override fun compareTo(other: Time): Int = (hour - other.hour) * 60 + minute - other.minute
}

/**
 * Остановка (название, время прибытия)
 */
data class Stop(val name: String, val time: Time)

/**
 * Поезд (имя, список остановок, упорядоченный по времени).
 * Первой идёт начальная остановка, последней конечная.
 */
data class Train(val name: String, val stops: List<Stop>) {
    constructor(name: String, vararg stops: Stop) : this(name, stops.asList())

    fun specificStation(name: String): Stop? = stops.find { it.name == name }
}
