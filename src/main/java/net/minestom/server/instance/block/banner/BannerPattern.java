package net.minestom.server.instance.block.banner;

import net.minestom.server.network.NetworkBuffer;
import net.minestom.server.registry.DynamicRegistry;
import net.minestom.server.registry.ProtocolObject;
import net.minestom.server.registry.Registries;
import net.minestom.server.registry.Registry;
import net.minestom.server.utils.NamespaceID;
import net.minestom.server.utils.nbt.BinaryTagSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public sealed interface BannerPattern extends ProtocolObject, BannerPatterns permits BannerPatternImpl {
    @NotNull NetworkBuffer.Type<DynamicRegistry.Key<BannerPattern>> NETWORK_TYPE = NetworkBuffer.RegistryKey(Registries::bannerPattern);
    @NotNull BinaryTagSerializer<DynamicRegistry.Key<BannerPattern>> NBT_TYPE = BinaryTagSerializer.registryKey(Registries::bannerPattern);

    static @NotNull BannerPattern create(
            NamespaceID namespace,
            @NotNull NamespaceID assetId,
            @NotNull String translationKey
    ) {
        return new BannerPatternImpl(namespace, assetId, translationKey, null);
    }

    static @NotNull Builder builder(NamespaceID namespace) {
        return new Builder(namespace);
    }

    /**
     * <p>Creates a new registry for banner patterns, loading the vanilla banner patterns.</p>
     *
     * @see net.minestom.server.MinecraftServer to get an existing instance of the registry
     */
    @ApiStatus.Internal
    static @NotNull DynamicRegistry<BannerPattern> createDefaultRegistry() {
        final List<BannerPattern> bannerPatterns = Registry.loadRegistry(Registry.Resource.BANNER_PATTERNS, Registry.BannerPatternEntry::new).stream()
                .<BannerPattern>map(BannerPatternImpl::new).toList();
        return DynamicRegistry.create("minecraft:banner_pattern", BannerPatternImpl.REGISTRY_NBT_TYPE, bannerPatterns);
    }

    @NotNull NamespaceID assetId();

    @NotNull String translationKey();

    @Nullable Registry.BannerPatternEntry registry();

    final class Builder {
        private final NamespaceID namespace;
        private NamespaceID assetId;
        private String translationKey;

        private Builder(NamespaceID namespace) {
            this.namespace = namespace;
        }

        @Contract(value = "_ -> this", pure = true)
        public @NotNull Builder assetId(@NotNull NamespaceID assetId) {
            this.assetId = assetId;
            return this;
        }

        @Contract(value = "_ -> this", pure = true)
        public @NotNull Builder translationKey(@NotNull String translationKey) {
            this.translationKey = translationKey;
            return this;
        }

        @Contract(pure = true)
        public @NotNull BannerPattern build() {
            return new BannerPatternImpl(namespace, assetId, translationKey, null);
        }
    }
}
