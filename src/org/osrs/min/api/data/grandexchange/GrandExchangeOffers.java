package org.osrs.min.api.data.grandexchange;

import org.osrs.min.api.data.Game;
import org.osrs.min.api.wrappers.GrandExchangeOffer;
import org.osrs.min.loading.Loader;
import org.parabot.environment.api.utils.Filter;

import java.util.ArrayList;
import java.util.List;

public class GrandExchangeOffers {

    public static GrandExchangeOffer[] getOffers(Filter<GrandExchangeOffer> filter) {
        final List<GrandExchangeOffer> list = new ArrayList<>();
        if (!Game.isLoggedIn())
            return list.toArray(new GrandExchangeOffer[list.size()]);
        final org.osrs.min.api.accessors.GrandExchangeOffer[] offers = Loader.getClient().getGrandExchangeOffers();
        int i = 0;
        for (org.osrs.min.api.accessors.GrandExchangeOffer offer : offers) {
            if (offer != null) {
                final GrandExchangeOffer geOffer = new GrandExchangeOffer(offer, i);
                if ((filter == null || filter.accept(geOffer))) {
                    list.add(geOffer);
                }
            }
            i++;
        }
        return list.toArray(new GrandExchangeOffer[list.size()]);
    }

    public static GrandExchangeOffer[] getOffers() {
        final GrandExchangeOffer[] offers = getOffers(n -> true);
        if (offers == null) return new GrandExchangeOffer[0];
        return offers;
    }

    public static GrandExchangeOffer getOffer(Filter<GrandExchangeOffer> filter) {
        final GrandExchangeOffer[] offers = getOffers(filter);
        if (offers == null) return null;
        return offers[0];
    }

    public static GrandExchangeOffer[] getOffers(GrandExchangeOffer.OfferType type) {
        return GrandExchangeOffers.getOffers(offer -> {
                    return offer.getType() == type;
                }
        );
    }
}
