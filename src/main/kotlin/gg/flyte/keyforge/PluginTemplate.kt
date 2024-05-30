package gg.flyte.keyforge

import cloud.keyforge.keyforge.KeyforgeAPI
import com.google.gson.Gson
import gg.flyte.keyforge.command.SetKeyCommand
import gg.flyte.keyforge.listener.PlayerListeners
import gg.flyte.twilight.twilight
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler

class PluginTemplate : JavaPlugin() {

    companion object {
        lateinit var instance: PluginTemplate
    }

    override fun onEnable() {
        saveDefaultConfig()
        config.options().copyDefaults(true)

        checkLicense(this) { valid ->
            if (!valid) {
                server.pluginManager.disablePlugin(this)
                throw IllegalStateException("Invalid license key or API ID")
            } else {
                instance = this
                twilight(this) { }

                BukkitCommandHandler.create(this).apply {
                    enableAdventure()
                    register(SetKeyCommand())
                    registerBrigadier()
                }

                PlayerListeners()
            }
        }
    }
}

fun checkLicense(plugin: JavaPlugin, callback: (Boolean) -> Unit) {
    try {
        val apiId = plugin.config.getString("api-id") ?: return callback(false)
        val key = plugin.config.getString("key") ?: return callback(false)

        KeyforgeAPI.verifyKey(key, apiId).let { response ->
            if (response.valid) return callback(true)
            else throw IllegalStateException("Invalid license key or API ID")

        }
    } catch (e: Exception) { return callback(false) }
}