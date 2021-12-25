package advent2021

import java.awt.Color
import java.awt.Graphics
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.SwingUtilities
import kotlin.math.log
import kotlin.math.roundToLong

/** A GUI for graphing the output of the mysterious algorithm for Day 24 */
class Visualizer(
    private val start: Long = 0L,
    private val end: Long = 22876792454960L,
    private val function: (Long) -> Long,
    private val buildInput: (Long) -> String
) : JPanel() {

    private val maxRadius = end / 2
    private var center = (start + end) / 2
    private var zoom = 1.0

    fun display() {
        SwingUtilities.invokeLater { render() }
    }

    private fun render() {
        val f = JFrame("Day 24")
        f.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        f.setSize(250, 250)
        f.isVisible = true
        f.add(this)
        addMouseListener(object : MouseAdapter() {
            override fun mouseClicked(e: MouseEvent) {
                when (e.button) {
                    1 -> {
                        val percentage = e.x / width.toDouble()
                        center += ((percentage - 0.5) * (maxRadius / zoom)).roundToLong()
                        if (maxRadius / zoom > width / 2) {
                            zoom *= 2
                        }
                    }
                    3 -> {
                        zoom /= 2
                        if (zoom < 1.1) {
                            zoom = 1.0
                            center = (start + end) / 2
                        }
                    }
                    else -> {
                        center = 6380635214483
                    }
                }

                f.repaint()
            }
        })
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.clearRect(0, 0, width, height)
        val width = width.toDouble()
        val radius = maxRadius / zoom
        (0 until getWidth()).forEach { x ->
            val percentage = x / width
            val input = if (radius > width / 2) {
                (center + (percentage - 0.5) * radius).roundToLong()
            } else {
                center + x - getWidth() / 2
            }
            val output = function(input)
            val barHeight = if (output < height) {
                g.color = Color.RED
                println("$input -> ${buildInput(input)} -> $output")
                output.toInt()
            } else {
                g.color = Color.BLACK
                (log(output.toDouble(), 2.0).times(30).coerceAtMost(height.toDouble())).toInt()
            }
            g.drawLine(x, height, x, height - barHeight)
            if (output >= height) {
                g.color = Color.LIGHT_GRAY
                g.drawLine(x, height - barHeight, x, height - barHeight - (output % 100).toInt())
            }
        }
        g.color = Color.BLACK
        g.drawString(buildInput(center - (maxRadius / zoom).roundToLong()), 10, 10)
        g.drawString(buildInput(center + (maxRadius / zoom).roundToLong()), getWidth() - 120, 10)
        println()
    }

}