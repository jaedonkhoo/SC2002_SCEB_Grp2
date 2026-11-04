public class Host extends SeatingStaff {
    @Override
    protected ActionResult alreadyActedResult() {
        return ActionResult.HOST_ALREADY_ACTED;
    }
}
