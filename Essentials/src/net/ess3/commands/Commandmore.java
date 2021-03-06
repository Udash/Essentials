package net.ess3.commands;

import java.util.Locale;
import static net.ess3.I18n._;
import net.ess3.api.ISettings;
import net.ess3.api.IUser;
import net.ess3.permissions.Permissions;
import org.bukkit.inventory.ItemStack;


public class Commandmore extends EssentialsCommand
{
	@Override
	public void run(final IUser user, final String commandLabel, final String[] args) throws Exception
	{
		final ItemStack stack = user.getPlayer().getItemInHand();
		if (stack == null)
		{
			throw new Exception(_("cantSpawnItem", "Air"));
		}
		ISettings settings = ess.getSettings();

		int defaultStackSize = settings.getData().getGeneral().getDefaultStacksize();
		int oversizedStackSize = settings.getData().getGeneral().getOversizedStacksize();

		if (stack.getAmount() >= (Permissions.OVERSIZEDSTACKS.isAuthorized(user)
								  ? oversizedStackSize
								  : defaultStackSize > 0 ? defaultStackSize : stack.getMaxStackSize()))
		{
			throw new NoChargeException();
		}
		final String itemname = stack.getType().toString().toLowerCase(Locale.ENGLISH).replace("_", "");
		if (!Permissions.ITEMSPAWN.isAuthorized(user, stack))
		{
			throw new Exception(_("cantSpawnItem", itemname));
		}
		if (Permissions.OVERSIZEDSTACKS.isAuthorized(user))
		{
			stack.setAmount(oversizedStackSize);
		}
		else
		{
			stack.setAmount(defaultStackSize > 0 ? defaultStackSize : stack.getMaxStackSize());
		}
		user.getPlayer().updateInventory();
	}
}