public abstract class ControllableStaff {
    private boolean acted;

    public boolean hasActed() {
        return acted;
    }

    public void resetForNewTurn() {
        acted = false;
    }

    public ActionResult skipTurn() {
        if (hasActed()) {
            return alreadyActedResult();
        }

        markActed();
        return ActionResult.SUCCESS;
    }

    protected void markActed() {
        acted = true;
    }

    protected abstract ActionResult alreadyActedResult();
}
