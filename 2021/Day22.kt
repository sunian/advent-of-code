package advent2021

/** Day 22: Reactor Reboot */
fun main() {
    part1()
    part2()
}

private fun part2() {
    var cuboids = emptyList<Cuboid>()
    instructions.forEach { instruction ->
        val newCuboid = Cuboid(instruction.x, instruction.y, instruction.z)
        cuboids = cuboids.flatMap { it.remove(newCuboid) } +
                when {
                    instruction.turnOn -> listOf(newCuboid)
                    else -> emptyList()
                }
    }
    println(cuboids.sumOf { it.volume() })
}

private data class Cuboid(
    val x: IntRange,
    val y: IntRange,
    val z: IntRange
) {
    fun intersects(other: Cuboid): Boolean =
        this.x.intersects(other.x) && this.y.intersects(other.y) && this.z.intersects(other.z)

    private fun contains(other: Cuboid): Boolean =
        this.x.contains(other.x) && this.y.contains(other.y) && this.z.contains(other.z)

    /** Start with [this] cuboid and remove its intersection with [other] cuboid.
     * The remaining solid can be represented as a list of smaller cuboids. */
    fun remove(other: Cuboid): List<Cuboid> {
        if (!intersects(other)) {
            // [this] and [other] don't touch, so the entirety of [this] cuboid is preserved
            return listOf(this)
        }
        if (other.contains(this)) {
            // [this] cuboid is entirely inside [other], so nothing remains
            return emptyList()
        }
        val xRanges = splitUnion(this.x, other.x)
        val yRanges = splitUnion(this.y, other.y)
        val zRanges = splitUnion(this.z, other.z)
        // take the union of [this] and [other] and split it into small slices
        val result = (xRanges * yRanges * zRanges)
            .map { Cuboid(it.x, it.y, it.z) }
            .filter {
                // only keep the slices that are inside [this] but not inside [other]
                it.intersects(this) && !it.intersects(other)
            }.toMutableList()
        // repeatedly merge slices together whenever possible until we find
        // the minimal number of slices needed to represent the remaining solid
        while (true) {
            (result * result).firstOrNull { (cuboid1, cuboid2) ->
                // find a pair of different slices that can be merged into a single cuboid
                cuboid1 != cuboid2 && cuboid1.merge(cuboid2) != null
            }?.let { (cuboid1, cuboid2) ->
                // replace the 2 slices with their merge
                result.remove(cuboid1)
                result.remove(cuboid2)
                result.add(cuboid1.merge(cuboid2)!!)
            } ?: break
        }
        return result
    }

    /** split the union of 2 ranges into a list of contiguous subranges */
    private fun splitUnion(myRange: IntRange, otherRange: IntRange): List<IntRange> {
        val list = arrayListOf<IntRange>()
        val points = setOf(myRange.first, myRange.last, otherRange.first, otherRange.last)
        list.addAll(points.sorted().windowed(2).map { IntRange(it.first() + 1, it.last() - 1) })
        list.addAll(points.map { it..it })
        return list.filterNot { it.isEmpty() }
    }

    private fun merge(other: Cuboid): Cuboid? = when {
        x == other.x && y == other.y && z.isAdjacent(other.z) -> Cuboid(x, y, z + other.z)
        x == other.x && z == other.z && y.isAdjacent(other.y) -> Cuboid(x, y + other.y, z)
        y == other.y && z == other.z && x.isAdjacent(other.x) -> Cuboid(x + other.x, y, z)
        else -> null
    }

    fun volume() = x.size().toLong() * y.size().toLong() * z.size().toLong()
}

private fun part1() {
    val grid = hashSetOf<String>()
    val interestedRange = -50..50
    instructions.forEach { instruction ->
        val xRange = instruction.x.intersection(interestedRange)
        val yRange = instruction.y.intersection(interestedRange)
        val zRange = instruction.z.intersection(interestedRange)
        if (xRange.isEmpty() || yRange.isEmpty() || zRange.isEmpty()) {
            return@forEach
        }
        xRange.forEach { x ->
            yRange.forEach { y ->
                zRange.forEach { z ->
                    val point = "$x,$y,$z"
                    if (instruction.turnOn) {
                        grid.add(point)
                    } else {
                        grid.remove(point)
                    }
                }
            }
        }
    }
    println(grid.size)
}

private data class Instruction(
    val turnOn: Boolean,
    val x: IntRange,
    val y: IntRange,
    val z: IntRange,
)

private val instructions = arrayListOf(
    Instruction(true, x = -1..48, y = -16..38, z = -20..25),
    Instruction(true, x = -18..29, y = -44..3, z = -10..43),
    Instruction(true, x = -34..16, y = -15..32, z = -46..4),
    Instruction(true, x = -22..27, y = -19..33, z = -7..47),
    Instruction(true, x = -19..31, y = -31..20, z = -18..32),
    Instruction(true, x = -37..9, y = -11..35, z = -17..27),
    Instruction(true, x = -41..13, y = -41..13, z = -14..37),
    Instruction(true, x = -12..37, y = -37..7, z = -2..46),
    Instruction(true, x = -17..27, y = -22..23, z = -16..34),
    Instruction(true, x = -45..9, y = -20..30, z = -41..3),
    Instruction(false, x = 0..14, y = -3..6, z = -49..-30),
    Instruction(true, x = -19..35, y = -23..24, z = -41..7),
    Instruction(false, x = 2..18, y = -33..-18, z = -27..-17),
    Instruction(true, x = -26..27, y = -10..40, z = -20..30),
    Instruction(false, x = 10..21, y = -6..12, z = -31..-18),
    Instruction(true, x = -41..4, y = -48..6, z = -18..28),
    Instruction(false, x = -48..-37, y = -46..-28, z = 15..26),
    Instruction(true, x = -42..9, y = -3..41, z = 0..49),
    Instruction(false, x = -25..-14, y = -1..13, z = 17..28),
    Instruction(true, x = -6..48, y = -23..29, z = -40..13),
    Instruction(true, x = 33437..56338, y = 12827..43842, z = 45512..79325),
    Instruction(true, x = -17342..3165, y = -94930..-58559, z = -33356..-16702),
    Instruction(true, x = 59487..67989, y = -40780..-27724, z = 26492..52049),
    Instruction(true, x = 18242..35449, y = -37046..-9836, z = 49310..82186),
    Instruction(true, x = -27603..-6975, y = 49383..54200, z = 59064..77711),
    Instruction(true, x = 41065..67320, y = 59371..73606, z = -19211..7672),
    Instruction(true, x = -2549..20933, y = -63492..-41908, z = -70158..-52821),
    Instruction(true, x = -51910..-44518, y = 59764..62391, z = 22604..29458),
    Instruction(true, x = 56200..75731, y = -27746..-13374, z = -34980..-25179),
    Instruction(true, x = -7631..27550, y = -69774..-54066, z = -70650..-33166),
    Instruction(true, x = 27578..42851, y = -75597..-72343, z = -19560..16754),
    Instruction(true, x = -6878..11072, y = 42099..70170, z = 48782..73011),
    Instruction(true, x = 70156..94750, y = -70..25231, z = 948..14653),
    Instruction(true, x = -40062..-27035, y = -75430..-67698, z = 18543..33830),
    Instruction(true, x = -81636..-61039, y = 2866..18618, z = 15873..26478),
    Instruction(true, x = -72782..-63455, y = 4714..26418, z = -52162..-32914),
    Instruction(true, x = 70975..86474, y = -28052..-18354, z = -15892..10277),
    Instruction(true, x = -21601..6945, y = 50585..81690, z = -49699..-31684),
    Instruction(true, x = 20141..40434, y = -95907..-69218, z = -15108..11374),
    Instruction(true, x = 36908..69871, y = 32892..59654, z = 29200..57891),
    Instruction(true, x = 361..24312, y = 67326..72968, z = 20325..48008),
    Instruction(true, x = -69171..-55700, y = 40493..52477, z = -32109..-13746),
    Instruction(true, x = -55329..-32849, y = 18673..46758, z = -67551..-42790),
    Instruction(true, x = 33826..58282, y = 48321..74097, z = 18290..41319),
    Instruction(true, x = -27239..-8541, y = -22881..88, z = 73663..86430),
    Instruction(true, x = 26507..48226, y = 16841..28506, z = -69139..-53270),
    Instruction(true, x = 53809..76860, y = 13829..20865, z = -38477..-19649),
    Instruction(true, x = 31042..57342, y = -75797..-51877, z = 6810..22228),
    Instruction(true, x = -57999..-41870, y = -63042..-48886, z = -52539..-19071),
    Instruction(true, x = -26910..-14235, y = -42398..-23278, z = 68243..86894),
    Instruction(true, x = 52252..74857, y = -19977..-8598, z = 40373..58714),
    Instruction(true, x = 50010..74284, y = 41007..44474, z = -4066..6641),
    Instruction(true, x = -49349..-30889, y = 36058..45942, z = -74841..-36743),
    Instruction(true, x = -66759..-32543, y = 48052..70654, z = 19792..34201),
    Instruction(true, x = -38195..-17986, y = -85103..-73270, z = -14419..2417),
    Instruction(true, x = -54218..-39823, y = 45436..73414, z = 3347..36613),
    Instruction(true, x = 7369..25671, y = -59478..-38270, z = -56879..-54151),
    Instruction(true, x = 2256..31577, y = -94146..-72126, z = -4317..5999),
    Instruction(true, x = -40900..-17098, y = -76036..-57685, z = -48786..-25459),
    Instruction(true, x = -95207..-73375, y = -9149..9428, z = 23275..38018),
    Instruction(true, x = -36850..-26928, y = -37292..-10502, z = 64014..81151),
    Instruction(true, x = -59033..-36526, y = 43232..65115, z = -55165..-32801),
    Instruction(true, x = -89130..-71599, y = 16261..29112, z = 13592..32099),
    Instruction(true, x = -35925..-23850, y = 30571..48852, z = 50456..69805),
    Instruction(true, x = 55748..82830, y = -41931..-36569, z = 20305..27083),
    Instruction(true, x = 2625..31989, y = -83863..-59167, z = 21008..35199),
    Instruction(true, x = -17120..8196, y = -15952..20689, z = -97634..-63082),
    Instruction(true, x = 67797..80569, y = -4947..4065, z = -23771..-13125),
    Instruction(true, x = 17722..26181, y = 63634..77078, z = -40399..-14266),
    Instruction(true, x = -24714..-17103, y = -58596..-33755, z = 38217..62404),
    Instruction(true, x = -35587..-23621, y = 46012..80548, z = 26588..46795),
    Instruction(true, x = 31936..40355, y = -67030..-49381, z = -50268..-14784),
    Instruction(true, x = 5596..13661, y = -74958..-45275, z = 40407..69288),
    Instruction(true, x = 38612..67368, y = -60786..-37973, z = 23314..46473),
    Instruction(true, x = -72045..-52232, y = -21032..-8164, z = -52171..-23686),
    Instruction(true, x = -29968..-4151, y = -24768..8715, z = -89266..-60234),
    Instruction(true, x = -37509..-23170, y = 60310..83858, z = 28551..38985),
    Instruction(true, x = -68577..-35018, y = 2999..23020, z = 38096..58076),
    Instruction(true, x = -70238..-46751, y = -62914..-49025, z = -12150..18310),
    Instruction(true, x = -57173..-44897, y = -66415..-50723, z = -12817..4244),
    Instruction(true, x = -13706..1019, y = 28466..57936, z = -74905..-49591),
    Instruction(true, x = -72607..-45585, y = -71863..-37894, z = -41318..-24112),
    Instruction(true, x = 54095..74330, y = -70866..-44913, z = -16762..-4913),
    Instruction(true, x = 50668..74672, y = -52426..-21786, z = 25368..44293),
    Instruction(true, x = -7034..20115, y = -9693..17713, z = -85683..-66007),
    Instruction(true, x = 50471..66846, y = 27203..61489, z = 4211..19870),
    Instruction(true, x = -45900..-8537, y = 56117..84621, z = 15593..49599),
    Instruction(true, x = -57555..-29431, y = -23406..-1928, z = -76476..-47096),
    Instruction(true, x = 13660..32420, y = -17969..-10006, z = -96788..-60606),
    Instruction(true, x = 66346..96729, y = 9181..27708, z = -16591..11613),
    Instruction(true, x = -78750..-61738, y = 17521..50078, z = -33313..-12817),
    Instruction(true, x = -1994..12887, y = 1003..26974, z = -81281..-61765),
    Instruction(true, x = -78118..-47747, y = -36234..-13180, z = -58773..-43656),
    Instruction(true, x = 8567..29515, y = 67122..95882, z = 9275..32341),
    Instruction(true, x = -77707..-61614, y = 14468..31616, z = 31062..56551),
    Instruction(true, x = -41615..-19627, y = 28997..35740, z = -81998..-65477),
    Instruction(true, x = -48052..-38972, y = -68139..-45048, z = 31724..39389),
    Instruction(true, x = -23082..2058, y = 59185..88471, z = 32749..48405),
    Instruction(true, x = -75158..-50341, y = -13609..6898, z = -46696..-37869),
    Instruction(true, x = -32414..-20971, y = 11061..41294, z = 61244..84706),
    Instruction(true, x = 4815..30502, y = -30209..-12179, z = -96509..-71288),
    Instruction(true, x = -37897..-7477, y = -39767..-16117, z = 64382..74721),
    Instruction(true, x = 52144..72942, y = 17844..45538, z = -58438..-25227),
    Instruction(true, x = -49398..-12795, y = 56353..74616, z = 9023..26905),
    Instruction(true, x = 46915..68081, y = 11021..16074, z = -66835..-44586),
    Instruction(true, x = 23443..44134, y = 59717..72628, z = -5705..25287),
    Instruction(true, x = -51367..-31498, y = 53972..66920, z = -33369..-5601),
    Instruction(true, x = 64880..87795, y = -25662..1781, z = 2696..31936),
    Instruction(true, x = -70854..-33390, y = 39648..40840, z = -50688..-37989),
    Instruction(true, x = 23908..47949, y = -82674..-48695, z = 20546..41974),
    Instruction(true, x = 46954..51195, y = 39161..56472, z = 37602..51128),
    Instruction(true, x = 11517..29536, y = -29920..-8358, z = 68433..84983),
    Instruction(true, x = -94466..-57419, y = -13379..8308, z = 6519..27984),
    Instruction(true, x = -54018..-31105, y = 20101..39414, z = -83628..-50027),
    Instruction(true, x = -85145..-58647, y = 13861..36615, z = 2945..17633),
    Instruction(true, x = -43689..-27994, y = -81519..-68896, z = -5565..18293),
    Instruction(true, x = -57442..-29781, y = -75713..-51718, z = -19561..10528),
    Instruction(true, x = -96614..-71443, y = 4298..19595, z = -15543..-8479),
    Instruction(true, x = -4983..6836, y = -73083..-52295, z = -53115..-19323),
    Instruction(true, x = -87354..-57072, y = -53989..-20809, z = -28499..7938),
    Instruction(true, x = 44429..59086, y = -17701..18642, z = 54239..70453),
    Instruction(true, x = -7490..9995, y = -51625..-28653, z = 51656..86887),
    Instruction(true, x = 6333..20981, y = -96081..-59302, z = -7405..3774),
    Instruction(true, x = -43893..-20347, y = 46567..66155, z = -53307..-38008),
    Instruction(true, x = 57283..76995, y = 39798..52904, z = -34469..-27279),
    Instruction(true, x = -31364..-11438, y = 22411..40749, z = -70531..-66561),
    Instruction(true, x = 53718..88004, y = -40108..-9779, z = 991..28453),
    Instruction(true, x = -17651..4676, y = 58117..82206, z = -58760..-48210),
    Instruction(true, x = -439..6290, y = -58250..-44204, z = -76031..-47610),
    Instruction(true, x = -44688..-41724, y = 60455..73374, z = 15636..31159),
    Instruction(true, x = -28875..-22544, y = -82606..-52627, z = -45244..-32570),
    Instruction(true, x = -15984..-6960, y = -23413..-4029, z = 60645..95782),
    Instruction(true, x = -61769..-28435, y = 57924..68804, z = 9953..16109),
    Instruction(true, x = -67804..-42717, y = -57246..-41937, z = -38286..-12750),
    Instruction(true, x = 58507..68248, y = 35220..59797, z = -877..15963),
    Instruction(true, x = -30359..-7600, y = -2344..29713, z = -91157..-73720),
    Instruction(true, x = -79799..-68763, y = 9997..27685, z = 4601..27347),
    Instruction(true, x = -35384..-1568, y = -81750..-68689, z = 369..26982),
    Instruction(true, x = -64852..-51910, y = -21519..7739, z = 47934..74956),
    Instruction(true, x = 39228..72609, y = 34051..61731, z = 15726..47402),
    Instruction(true, x = 54184..83485, y = -23478..4300, z = 19603..48425),
    Instruction(true, x = 16735..43379, y = -46508..-11847, z = 60832..82954),
    Instruction(true, x = -45821..-31159, y = 62555..90058, z = 5642..13116),
    Instruction(true, x = 73298..99452, y = -12818..12447, z = -12241..17805),
    Instruction(true, x = -54926..-45244, y = -19135..-3467, z = 58668..66114),
    Instruction(true, x = -56698..-42018, y = -55343..-42385, z = -37607..-8542),
    Instruction(true, x = 59307..81618, y = 19362..27550, z = 15471..39075),
    Instruction(true, x = -71448..-33061, y = -38627..-35422, z = -67952..-36183),
    Instruction(true, x = -83215..-59852, y = 16889..41028, z = -56..32938),
    Instruction(true, x = 40526..68326, y = -2472..24699, z = 56335..66875),
    Instruction(true, x = -77946..-70763, y = 25469..43508, z = 2951..14856),
    Instruction(true, x = -46543..-17914, y = 58749..73413, z = -16218..-3353),
    Instruction(true, x = 35355..53713, y = -68542..-50644, z = -51561..-25775),
    Instruction(true, x = -32356..-10346, y = 22323..30374, z = -93261..-56997),
    Instruction(true, x = -73600..-57992, y = -55157..-44598, z = -14118..17601),
    Instruction(true, x = -50437..-34207, y = -48988..-34142, z = 27358..61321),
    Instruction(true, x = -47427..-36307, y = 52468..73774, z = 30289..48668),
    Instruction(true, x = 6660..33981, y = 24092..44706, z = 58492..92857),
    Instruction(true, x = -30628..-12500, y = -46167..-17405, z = 59394..83060),
    Instruction(true, x = 41538..55191, y = 43501..60608, z = -40139..-31035),
    Instruction(true, x = -29538..-3412, y = 74927..77766, z = -2541..15551),
    Instruction(true, x = 15784..34520, y = 24278..54221, z = 65089..75147),
    Instruction(true, x = 42535..56644, y = -1335..23855, z = -72076..-42080),
    Instruction(true, x = -35738..-25484, y = 29658..50334, z = 49522..80528),
    Instruction(true, x = 40838..63075, y = -68471..-44781, z = -27018..-4034),
    Instruction(true, x = -86197..-58456, y = 4038..9789, z = 23939..38730),
    Instruction(true, x = -50978..-37282, y = 18238..33963, z = -68013..-46889),
    Instruction(true, x = -9002..18414, y = 57350..65842, z = -64350..-45437),
    Instruction(true, x = 15643..40411, y = 66071..79781, z = 7607..19783),
    Instruction(true, x = 16474..33177, y = 64402..91823, z = -10253..-5099),
    Instruction(true, x = -8443..8050, y = 51848..81740, z = -58521..-33652),
    Instruction(true, x = -60748..-54505, y = -29567..-14412, z = -56374..-33973),
    Instruction(true, x = -55852..-50340, y = -1062..29335, z = -62774..-52740),
    Instruction(true, x = 27118..60720, y = -59485..-48094, z = -42684..-33215),
    Instruction(true, x = -6113..20676, y = 23469..40315, z = 64560..85647),
    Instruction(true, x = 26071..51400, y = -5826..20371, z = -77828..-70855),
    Instruction(true, x = -74360..-66078, y = -8194..17627, z = 30575..35854),
    Instruction(true, x = 40297..50534, y = 61080..77216, z = -18473..-13420),
    Instruction(true, x = 6481..32188, y = -73038..-67468, z = -37425..-17822),
    Instruction(true, x = 69163..81923, y = 4794..11958, z = 14398..31408),
    Instruction(true, x = 36923..59329, y = -65909..-48917, z = -1373..22196),
    Instruction(true, x = -33381..-13509, y = 30568..40854, z = 46822..70376),
    Instruction(true, x = 59457..79154, y = 29600..51339, z = 444..3489),
    Instruction(true, x = -51419..-24415, y = 13429..36159, z = 52009..71526),
    Instruction(true, x = -17852..4277, y = 27227..50102, z = -85725..-70528),
    Instruction(true, x = 43063..62237, y = -35715..-16381, z = -46313..-39565),
    Instruction(true, x = 1241..7407, y = -29526..-9534, z = -76285..-63119),
    Instruction(true, x = 57223..69361, y = -33985..-25987, z = -56817..-31108),
    Instruction(true, x = -28082..-15386, y = 56805..83538, z = -47032..-11458),
    Instruction(true, x = 29138..58816, y = 46528..60618, z = -44786..-15056),
    Instruction(true, x = 66453..81884, y = -10034..20560, z = -29536..-7062),
    Instruction(true, x = 1600..24056, y = 69703..97557, z = -16476..16237),
    Instruction(true, x = -37847..-14432, y = -72282..-68661, z = -35526..-13599),
    Instruction(true, x = -8868..13571, y = -41127..-22602, z = 70177..86033),
    Instruction(true, x = 61524..91545, y = -21808..1174, z = 29652..38701),
    Instruction(true, x = 58060..78978, y = -48311..-38908, z = -19604..4587),
    Instruction(true, x = 51890..79701, y = -53117..-20189, z = 5288..27256),
    Instruction(true, x = -56628..-33308, y = -75005..-55947, z = 6100..24176),
    Instruction(true, x = -476..19625, y = -79546..-56031, z = 35565..51238),
    Instruction(true, x = -25721..-18051, y = 47720..69868, z = -59102..-29211),
    Instruction(true, x = 44706..54461, y = 49550..67889, z = -18248..543),
    Instruction(true, x = -34739..-13770, y = 39347..72870, z = 39407..71230),
    Instruction(true, x = -67763..-32410, y = 45951..75648, z = -32728..-18587),
    Instruction(true, x = 42494..63426, y = -71528..-40260, z = -7254..17963),
    Instruction(true, x = -76582..-60188, y = -55062..-24896, z = -25573..3064),
    Instruction(true, x = 21953..37769, y = -75645..-54247, z = -35308..-19223),
    Instruction(true, x = 47005..61959, y = -75258..-55850, z = -35024..-22448),
    Instruction(true, x = -65676..-35556, y = -2796..16725, z = -71103..-42080),
    Instruction(true, x = -48829..-30079, y = 18210..34501, z = 52882..66667),
    Instruction(true, x = -42810..-31723, y = 55129..68535, z = 22348..45500),
    Instruction(true, x = -37433..-25195, y = -38957..-23637, z = -68796..-61046),
    Instruction(true, x = -82289..-62535, y = -2433..17631, z = 5359..26886),
    Instruction(true, x = -7504..10315, y = -92548..-76467, z = -28253..264),
    Instruction(true, x = 40755..66162, y = -42861..-40086, z = 33848..52972),
    Instruction(true, x = 41251..59301, y = -36909..-31405, z = 48865..70055),
    Instruction(true, x = -77381..-52063, y = 22297..42679, z = -17532..3121),
    Instruction(true, x = -41142..-23099, y = 22442..43961, z = -82656..-61658),
    Instruction(true, x = 50872..58507, y = 41441..77408, z = -3646..8460),
    Instruction(true, x = 22531..42190, y = 48136..66866, z = -35736..-15654),
    Instruction(true, x = -26349..-4775, y = -39232..-3391, z = -80767..-62478),
    Instruction(true, x = -22714..4657, y = -40195..-17309, z = -84682..-57245),
    Instruction(true, x = -79436..-46156, y = -44726..-27093, z = 33957..37913),
    Instruction(false, x = -65503..-29447, y = -61594..-40102, z = -61417..-45704),
    Instruction(true, x = -5465..14206, y = 24875..54603, z = 70363..72584),
    Instruction(false, x = -90679..-68512, y = -16733..-2366, z = -17803..-459),
    Instruction(true, x = 34647..50336, y = -370..18869, z = 50354..79403),
    Instruction(false, x = -82737..-67740, y = -21454..-13408, z = -6599..24650),
    Instruction(false, x = -7969..8415, y = 24338..38623, z = -85609..-72504),
    Instruction(false, x = 60119..85298, y = -11772..6088, z = -35654..-24544),
    Instruction(false, x = -1482..27629, y = 58542..72017, z = 43264..52093),
    Instruction(true, x = 48241..62254, y = 44558..65621, z = 12800..49591),
    Instruction(true, x = -61661..-55230, y = 6683..38408, z = 42243..63686),
    Instruction(true, x = -89994..-66991, y = -5015..1956, z = 12115..34030),
    Instruction(true, x = -13384..12270, y = 60123..76594, z = -39329..-26189),
    Instruction(false, x = -83463..-73162, y = 10218..37682, z = 18999..41815),
    Instruction(false, x = -66935..-50802, y = 57969..72430, z = -4335..22504),
    Instruction(false, x = -96282..-60896, y = -26066..-14534, z = -24372..4521),
    Instruction(true, x = 23742..37777, y = -7750..7548, z = 75456..82421),
    Instruction(false, x = 18943..39318, y = -32026..-3548, z = 49723..70085),
    Instruction(true, x = 6657..31107, y = -55214..-35170, z = 54123..78681),
    Instruction(true, x = 30268..47437, y = 57337..79545, z = 7368..29319),
    Instruction(false, x = -50998..-28101, y = 36476..61906, z = -64012..-50549),
    Instruction(false, x = -39601..-31261, y = -84997..-47856, z = 22833..24054),
    Instruction(false, x = -27590..3082, y = -85991..-75125, z = -19194..1655),
    Instruction(false, x = -86685..-67128, y = -26451..-10305, z = 16483..39732),
    Instruction(true, x = -62079..-52534, y = -585..28826, z = 37527..56387),
    Instruction(false, x = 52158..82528, y = 32115..40848, z = -31876..-5720),
    Instruction(false, x = 56731..68604, y = -49499..-38435, z = -41863..-12658),
    Instruction(true, x = -20662..16727, y = -46058..-35895, z = -85887..-52878),
    Instruction(true, x = -89787..-74154, y = 4894..29437, z = -9810..3488),
    Instruction(true, x = -7126..23121, y = -94245..-60012, z = -31384..-23826),
    Instruction(false, x = 53735..66601, y = -17438..-3284, z = 42673..56709),
    Instruction(false, x = -52698..-28631, y = 47999..73584, z = 7259..24908),
    Instruction(false, x = 22906..37342, y = -31575..-20580, z = 67513..82493),
    Instruction(false, x = 21513..40526, y = 16515..21005, z = 65952..73402),
    Instruction(false, x = -74708..-51479, y = 36496..61930, z = -45185..-29629),
    Instruction(true, x = -23445..5030, y = 40395..59020, z = -67250..-51869),
    Instruction(true, x = -58811..-30069, y = -75251..-54695, z = -26012..4790),
    Instruction(false, x = -37564..-28053, y = -64804..-42654, z = 29266..59561),
    Instruction(true, x = -92210..-73685, y = -42955..-22892, z = 3492..17965),
    Instruction(true, x = 24088..43347, y = 68563..85427, z = -30128..-5268),
    Instruction(false, x = -46774..-32427, y = 27060..53716, z = -56806..-39798),
    Instruction(true, x = -20970..-776, y = -20379..12252, z = 70235..90043),
    Instruction(false, x = 65519..92005, y = -29271..-15556, z = -11767..5023),
    Instruction(true, x = -24238..1445, y = -29150..-8012, z = -79236..-71936),
    Instruction(false, x = -16413..7485, y = 8784..20457, z = 73701..85140),
    Instruction(true, x = -81617..-72738, y = -6076..21804, z = 21184..42297),
    Instruction(true, x = -18603..3809, y = -83618..-65534, z = 15915..41820),
    Instruction(false, x = 46035..68548, y = -10891..7946, z = -50325..-48786),
    Instruction(false, x = -13877..4719, y = 49133..83943, z = 24223..49712),
    Instruction(false, x = -48296..-18478, y = 60823..88367, z = -36504..-19465),
    Instruction(true, x = 55508..82544, y = -53509..-28856, z = -10981..-3901),
    Instruction(true, x = -121..31007, y = -97086..-59816, z = 3028..24710),
    Instruction(false, x = 5607..26287, y = -7132..8390, z = -84301..-73477),
    Instruction(true, x = 27660..47152, y = -78844..-48390, z = -53615..-25669),
    Instruction(true, x = 26175..46398, y = -79682..-56410, z = -39939..-33070),
    Instruction(true, x = -716..17324, y = -35503..-7447, z = 64002..91036),
    Instruction(true, x = 29236..58575, y = -42872..-25966, z = -52993..-42312),
    Instruction(false, x = 66758..75691, y = 29380..46051, z = -31613..-4526),
    Instruction(true, x = -68224..-51823, y = -41140..-23551, z = -16856..1016),
    Instruction(false, x = -51897..-36523, y = 8713..40129, z = 41866..70172),
    Instruction(false, x = -22778..-8036, y = 60521..88507, z = 4490..20427),
    Instruction(true, x = 38888..63600, y = 48785..73192, z = -892..15178),
    Instruction(false, x = -36116..-24736, y = 7815..23583, z = 57438..73807),
    Instruction(true, x = 64214..77437, y = -18931..11171, z = -42603..-16165),
    Instruction(true, x = 10978..12022, y = -72200..-54254, z = -52274..-44793),
    Instruction(true, x = -60033..-47866, y = 789..23595, z = -73838..-55809),
    Instruction(true, x = 9958..26741, y = 6721..21385, z = -83084..-63871),
    Instruction(false, x = 66327..92775, y = -24230..5079, z = -8466..6328),
    Instruction(true, x = -1645..2031, y = -81512..-64734, z = 5292..29072),
    Instruction(true, x = 37006..58150, y = 65826..67459, z = -37153..-17354),
    Instruction(false, x = -56615..-45426, y = -66544..-40764, z = -35158..-20406),
    Instruction(false, x = -20465..-1645, y = 15854..46792, z = -78862..-57458),
    Instruction(true, x = -77298..-57411, y = -62753..-41400, z = 20804..39625),
    Instruction(false, x = 48718..69744, y = 12237..34810, z = 33525..45584),
    Instruction(false, x = -39587..-18804, y = -66746..-52585, z = -43094..-34404),
    Instruction(true, x = -3101..13941, y = 28196..45353, z = 62487..86239),
    Instruction(true, x = -75014..-64908, y = -9727..11801, z = 25982..43572),
    Instruction(true, x = 54237..70861, y = -54965..-30858, z = 23105..37368),
    Instruction(false, x = -14240..13016, y = 17905..36311, z = 60709..82665),
    Instruction(true, x = 53894..91359, y = -10908..136, z = -49530..-22631),
    Instruction(false, x = -59429..-31060, y = -67643..-49414, z = -39524..-29929),
    Instruction(false, x = -87527..-55043, y = 16513..41342, z = -37270..-9508),
    Instruction(true, x = -10043..8301, y = 14011..33754, z = 63873..96942),
    Instruction(true, x = 10118..26193, y = -76946..-67615, z = 26000..46974),
    Instruction(false, x = 41985..49081, y = -4796..25939, z = -67797..-54689),
    Instruction(true, x = 14002..31550, y = 45089..72540, z = 36401..72031),
    Instruction(true, x = -42408..-33667, y = 47820..69031, z = 24526..34790),
    Instruction(false, x = -16739..1068, y = 56466..82023, z = -53856..-29125),
    Instruction(false, x = 17727..40188, y = 43235..63199, z = 23764..37631),
    Instruction(true, x = -53053..-35579, y = 36615..57554, z = 58995..68523),
    Instruction(false, x = -58061..-30341, y = -51828..-32007, z = -63517..-28782),
    Instruction(true, x = 55984..67199, y = 1210..23837, z = 47014..64862),
    Instruction(false, x = -29228..-5835, y = -86197..-58626, z = -3753..18921),
    Instruction(false, x = -28618..-14264, y = 67121..95798, z = -20042..11478),
    Instruction(true, x = 30863..49426, y = -74491..-61218, z = -31130..-10777),
    Instruction(false, x = 28109..44394, y = -22301..-9661, z = -81353..-61616),
    Instruction(true, x = 26554..32009, y = 66503..83064, z = -12338..5592),
    Instruction(false, x = -77980..-54146, y = -51614..-34613, z = -42685..-15270),
    Instruction(false, x = -2779..16793, y = 68054..73592, z = -47923..-19467),
    Instruction(false, x = -22664..-11272, y = -24727..1390, z = -89458..-69365),
    Instruction(false, x = -13025..9517, y = -97732..-64371, z = -25225..-8991),
    Instruction(true, x = 41843..55691, y = -9251..1519, z = 43981..73893),
    Instruction(true, x = 27996..42516, y = 7602..27387, z = 60352..80458),
    Instruction(false, x = -33031..-820, y = -2237..3954, z = 76512..88569),
    Instruction(true, x = 53994..78980, y = 24993..50728, z = -32458..2463),
    Instruction(false, x = 31059..59642, y = 9695..23750, z = 49813..74453),
    Instruction(false, x = 14399..36596, y = -31161..-20384, z = 54604..79311),
    Instruction(false, x = -45811..-22101, y = 60940..72486, z = -42904..-32492),
    Instruction(false, x = 53723..71163, y = -63147..-42994, z = 17671..46914),
    Instruction(true, x = 70868..82593, y = 3609..20121, z = 17458..33961),
    Instruction(false, x = -78469..-59852, y = -37403..-8285, z = 14116..29529),
    Instruction(false, x = -90804..-60667, y = -7313..11998, z = -31738..6007),
    Instruction(false, x = 31624..40563, y = 23604..55286, z = -65520..-39493),
    Instruction(true, x = -74137..-63301, y = 29453..38579, z = -31212..-16750),
    Instruction(false, x = 30070..58931, y = 45451..76733, z = 19911..45199),
    Instruction(false, x = -42153..-9361, y = 7525..18513, z = -83720..-58077),
    Instruction(true, x = -56760..-40026, y = -63629..-50601, z = 3229..23310),
    Instruction(false, x = 6438..31573, y = 63132..91673, z = -22737..1687),
    Instruction(true, x = -2486..7619, y = -97706..-64418, z = -27914..2089),
    Instruction(false, x = 59078..73143, y = -52136..-25916, z = 22957..29241),
    Instruction(true, x = 43919..60337, y = -21332..-9954, z = 43328..57585),
    Instruction(true, x = -34725..-152, y = -93294..-61298, z = -15549..-2995),
    Instruction(true, x = 35727..38997, y = -14432..15654, z = 63548..85260),
    Instruction(false, x = -70864..-50832, y = -50076..-18495, z = -51915..-35025),
    Instruction(false, x = -90052..-64288, y = 17693..44173, z = 71..23706),
    Instruction(false, x = 39085..56978, y = 39661..61814, z = 6224..34271),
    Instruction(true, x = -31077..-21207, y = -78619..-57241, z = -9896..11390),
    Instruction(true, x = -66658..-47597, y = 11540..34770, z = 38391..65068),
    Instruction(true, x = -65615..-34509, y = 53414..70424, z = -8121..25175),
    Instruction(false, x = 22965..32240, y = 58946..87929, z = -10458..-4376),
    Instruction(false, x = 63082..90649, y = -26291..1752, z = 21476..39364),
    Instruction(true, x = 9524..32915, y = -43284..-19194, z = 52383..69188),
    Instruction(false, x = -51369..-17158, y = -74652..-49149, z = -31398..-10599),
    Instruction(false, x = 29266..33149, y = 7497..14985, z = 66875..82541),
    Instruction(true, x = -27021..-12645, y = 60715..79770, z = -43143..-24197),
    Instruction(true, x = -54871..-34848, y = -59775..-35252, z = -48344..-23443),
    Instruction(true, x = -1950..16881, y = 63179..86043, z = -55428..-31928),
    Instruction(false, x = 16581..29019, y = 61191..92312, z = 7013..32746),
    Instruction(false, x = -55445..-19252, y = -5710..23171, z = 58447..77784),
    Instruction(false, x = -17624..18337, y = -40609..-27297, z = 59683..76950),
    Instruction(false, x = 25862..54830, y = -75655..-55496, z = -361..24034),
    Instruction(true, x = 47642..63580, y = 13587..31697, z = -63323..-33822),
    Instruction(false, x = 45388..55145, y = -72791..-60442, z = -31428..-5005),
    Instruction(false, x = 47767..72283, y = -51375..-25540, z = 7381..25527),
    Instruction(false, x = -6322..30737, y = -25667..2565, z = -97128..-60724),
    Instruction(false, x = 65199..79046, y = 5420..37469, z = -31619..-15244),
    Instruction(false, x = 23572..39963, y = 63364..77180, z = 15360..35060),
    Instruction(true, x = -29512..126, y = -19605..-3604, z = -80094..-77214),
    Instruction(true, x = -85819..-49345, y = 13010..23934, z = -43932..-32506),
    Instruction(true, x = -73177..-44970, y = -8629..9092, z = 55299..71030),
    Instruction(true, x = 42496..64601, y = -55995..-41803, z = -30300..-10429),
    Instruction(false, x = -53775..-31854, y = -59234..-28407, z = -61605..-49173),
    Instruction(false, x = 31473..56209, y = 12988..21267, z = 53516..77383),
    Instruction(true, x = 29901..50061, y = 46738..79242, z = -38794..-9493),
    Instruction(true, x = 37285..54497, y = -65252..-36573, z = 16408..29558),
    Instruction(false, x = -12781..3406, y = -59504..-31950, z = -85822..-56333),
    Instruction(true, x = -37475..-13490, y = -10186..4616, z = -89147..-57477),
    Instruction(false, x = -47903..-22113, y = 43987..67652, z = 40223..42318),
    Instruction(false, x = -15046..18282, y = 13029..29067, z = -96462..-68422),
    Instruction(true, x = -83840..-69465, y = 451..22192, z = -34901..-9485),
    Instruction(false, x = -80409..-66287, y = -35747..-29770, z = 15839..24333),
    Instruction(true, x = -19010..750, y = -79337..-57187, z = -41849..-21549),
    Instruction(true, x = -48822..-21353, y = -33954..-94, z = 64810..72851),
    Instruction(false, x = -54657..-38467, y = -41458..-18427, z = 50354..63210),
    Instruction(false, x = 51408..81040, y = -14501..13586, z = 38332..47599),
    Instruction(false, x = 48954..76455, y = 3469..15311, z = -68958..-40888),
    Instruction(false, x = -58806..-47362, y = -76380..-51862, z = 5942..19718),
    Instruction(false, x = -53246..-26761, y = -77054..-52976, z = -22871..13576),
    Instruction(true, x = 27614..38741, y = 14613..29418, z = -83716..-57903),
    Instruction(false, x = 52319..72563, y = -6709..5215, z = 35214..64427),
    Instruction(false, x = -17272..15440, y = 45933..73436, z = -47268..-34777),
    Instruction(false, x = 4464..21409, y = 66927..90789, z = 7589..23185),
    Instruction(false, x = -6484..30730, y = -76775..-43599, z = -56202..-47419),
    Instruction(false, x = -47243..-26535, y = -20295..-811, z = -85189..-50716),
    Instruction(false, x = 1592..37371, y = -42895..-19259, z = 62255..81021),
    Instruction(false, x = -83413..-69227, y = -10276..19927, z = -17853..16589),
    Instruction(false, x = 11946..32712, y = 25300..45774, z = -74309..-63944),
    Instruction(false, x = 49376..83272, y = 7670..36706, z = 23521..50205),
    Instruction(false, x = -5750..17880, y = 60927..81058, z = 28301..34623),
    Instruction(true, x = -62883..-57040, y = -6621..6497, z = -58999..-47199),
    Instruction(false, x = 53793..81478, y = -1276..17782, z = 38884..44815),
    Instruction(true, x = 24651..48221, y = 50842..67067, z = 14049..42323),
    Instruction(false, x = 27311..46213, y = 33602..52451, z = -75659..-44109),
    Instruction(true, x = 61528..69908, y = -50915..-29489, z = 21046..41008),
    Instruction(true, x = 57155..69842, y = 25067..48665, z = 3868..24976),
    Instruction(true, x = -51744..-21639, y = -37864..-19572, z = -80353..-66632),
    Instruction(true, x = -53378..-38089, y = 38079..55906, z = 48582..54771),
    Instruction(false, x = -49276..-23705, y = 53316..86609, z = -21885..13301),
    Instruction(false, x = -27810..-6083, y = 1258..36348, z = 74783..87531),
    Instruction(false, x = 47836..81423, y = -20080..12837, z = -49106..-33359),
    Instruction(true, x = 43178..50772, y = -45368..-28229, z = -62038..-45931),
    Instruction(false, x = 69972..94011, y = -2896..7497, z = -18345..16328),
    Instruction(true, x = -41390..-27872, y = -11169..12857, z = 58269..88722),
    Instruction(false, x = -7635..13614, y = 47395..64088, z = -61661..-47174),
    Instruction(true, x = 15960..50155, y = 37826..70201, z = 32254..65902),
    Instruction(true, x = -35435..-4150, y = -36965..-34805, z = -80676..-66202),
    Instruction(false, x = 20573..26063, y = -30310..-12158, z = -89541..-58150),
    Instruction(false, x = 64081..88596, y = -25922..-5947, z = 16732..46694),
    Instruction(false, x = 79458..91210, y = -6760..4282, z = 1518..13350),
)